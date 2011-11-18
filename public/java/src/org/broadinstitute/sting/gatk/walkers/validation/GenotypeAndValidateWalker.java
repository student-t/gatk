/*
 * Copyright (c) 2010 The Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.broadinstitute.sting.gatk.walkers.validation;

import org.broadinstitute.sting.commandline.*;
import org.broadinstitute.sting.gatk.contexts.AlignmentContext;
import org.broadinstitute.sting.gatk.contexts.ReferenceContext;
import org.broadinstitute.sting.gatk.refdata.RefMetaDataTracker;
import org.broadinstitute.sting.gatk.walkers.*;
import org.broadinstitute.sting.gatk.walkers.genotyper.GenotypeLikelihoodsCalculationModel;
import org.broadinstitute.sting.gatk.walkers.genotyper.UnifiedArgumentCollection;
import org.broadinstitute.sting.gatk.walkers.genotyper.UnifiedGenotyperEngine;
import org.broadinstitute.sting.gatk.walkers.genotyper.VariantCallContext;
import org.broadinstitute.sting.utils.SampleUtils;
import org.broadinstitute.sting.utils.codecs.vcf.VCFHeader;
import org.broadinstitute.sting.utils.codecs.vcf.VCFHeaderLine;
import org.broadinstitute.sting.utils.codecs.vcf.VCFUtils;
import org.broadinstitute.sting.utils.codecs.vcf.VCFWriter;
import org.broadinstitute.sting.utils.variantcontext.VariantContext;
import org.broadinstitute.sting.utils.variantcontext.VariantContextBuilder;
import org.broadinstitute.sting.utils.variantcontext.VariantContextUtils;

import java.util.Map;
import java.util.Set;

import static org.broadinstitute.sting.utils.IndelUtils.isInsideExtendedIndel;

/**
 * Genotypes a dataset and validates the calls of another dataset using the Unified Genotyper.
 *
 *  <p>
 *     Genotype and Validate is a tool to evaluate the quality of a dataset for calling SNPs
 *     and Indels given a secondary (validation) data source. The data sources are BAM or VCF
 *     files. You can use them interchangeably (i.e. a BAM to validate calls in a VCF or a VCF
 *     to validate calls on a BAM).
 *  </p>
 *
 *  <p>
 *     The simplest scenario is when you have a VCF of hand annotated SNPs and Indels, and you
 *     want to know how well a particular technology performs calling these snps. With a
 *     dataset (BAM file) generated by the technology in test, and the hand annotated VCF, you
 *     can run GenotypeAndValidate to asses the accuracy of the calls with the new technology's
 *     dataset.
 *  </p>
 *
 *  <p>
 *     Another option is to validate the calls on a VCF file, using a deep coverage BAM file
 *     that you trust the calls on. The GenotypeAndValidate walker will make calls using the
 *     reads in the BAM file and take them as truth, then compare to the calls in the VCF file
 *     and produce a truth table.
 *  </p>
 *
 *
 * <h2>Input</h2>
 *  <p>
 *      A BAM file to make calls on and a VCF file to use as truth validation dataset.
 *
 *      You also have the option to invert the roles of the files using the command line options listed below.
 *  </p>
 *
 * <h2>Output</h2>
 *  <p>
 *      GenotypeAndValidate has two outputs. The truth table and the optional VCF file. The truth table is a
 *      2x2 table correlating what was called in the dataset with the truth of the call (whether it's a true
 *      positive or a false positive). The table should look like this:
 *  </p>
 * <center>
 * <table id="description-table">
 *     <tr>
 *         <th></th>
 *         <th>ALT</th>
 *         <th>REF</th>
 *         <th>Predictive Value</th>
 *     </tr>
 *     <tr>
 *         <td><b>called alt</b></td>
 *         <td>True Positive (TP)</td>
 *         <td>False Positive (FP)</td>
 *         <td>Positive PV</td>
 *     </tr>
 *     <tr>
 *         <td><b>called ref</b></td>
 *         <td>False Negative (FN)</td>
 *         <td>True Negative (TN)</td>
 *         <td>Negative PV</td>
 *     </tr>
 * </table>
 * </center>
 *
 *  <p>
 *      The <b>positive predictive value (PPV)</b> is the proportion of subjects with positive test results
 *      who are correctly diagnosed.
 *  </p>
 *  <p>
 *      The <b>negative predictive value (NPV)</b> is the proportion of subjects with a negative test result
 *      who are correctly diagnosed.
 *  </p>
 *  <p>
 *      The VCF file will contain only the variants that were called or not called, excluding the ones that
 *      were uncovered or didn't pass the filters. This file is useful if you are trying to compare
 *      the PPV and NPV of two different technologies on the exact same sites (so you can compare apples to
 *      apples).
 *  </p>
 *
 *  <p>
 *      Here is an example of an annotated VCF file (info field clipped for clarity)
 *
 * <pre>
 * #CHROM  POS ID  REF ALT QUAL    FILTER  INFO    FORMAT  NA12878
 * 1   20568807    .   C   T   0    HapMapHet        AC=1;AF=0.50;AN=2;DP=0;GV=T  GT  0/1
 * 1   22359922    .   T   C   282  WG-CG-HiSeq      AC=2;AF=0.50;GV=T;AN=4;DP=42 GT:AD:DP:GL:GQ  1/0 ./. 0/1:20,22:39:-72.79,-11.75,-67.94:99    ./.
 * 13  102391461   .   G   A   341  Indel;SnpCluster AC=1;GV=F;AF=0.50;AN=2;DP=45 GT:AD:DP:GL:GQ  ./. ./. 0/1:32,13:45:-50.99,-13.56,-112.17:99   ./.
 * 1   175516757   .   C   G   655  SnpCluster,WG    AC=1;AF=0.50;AN=2;GV=F;DP=74 GT:AD:DP:GL:GQ  ./. ./. 0/1:52,22:67:-89.02,-20.20,-191.27:99   ./.
 * </pre>
 *
 *  </p>
 *
 *  <h3>Additional Details</h3>
 *  <ul>
 *      <li>
 *          You should always use -L on your VCF track, so that the GATK only looks at the sites on the VCF file.
 *          This speeds up the process a lot.
 *      </li>
 *      <li>
 *          The total number of visited bases may be greater than the number of variants in the original
 *          VCF file because of extended indels, as they trigger one call per new insertion or deletion.
 *          (i.e. ACTG/- will count as 4 genotyper calls, but it's only one line in the VCF).
 *      </li>
 *  </ul>
 *
 * <h2>Examples</h2>
 * <ol>
 *     <li>
 *         Genotypes BAM file from new technology using the VCF as a truth dataset:
 *     </li>
 *
 * <pre>
 *  java
 *      -jar /GenomeAnalysisTK.jar
 *      -T  GenotypeAndValidate
 *      -R human_g1k_v37.fasta
 *      -I myNewTechReads.bam
 *      -alleles handAnnotatedVCF.vcf
 *      -L handAnnotatedVCF.vcf
 * </pre>
 *
 *      <li>
 *          Using a BAM file as the truth dataset:
 *      </li>
 *
 * <pre>
 *  java
 *      -jar /GenomeAnalysisTK.jar
 *      -T  GenotypeAndValidate
 *      -R human_g1k_v37.fasta
 *      -I myTruthDataset.bam
 *      -alleles callsToValidate.vcf
 *      -L callsToValidate.vcf
 *      -bt
 *      -o gav.vcf
 * </pre>
 *
 *
 * @author Mauricio Carneiro
 * @since ${DATE}
 */

@Requires(value={DataSource.READS, DataSource.REFERENCE})
@Allows(value={DataSource.READS, DataSource.REFERENCE})

@By(DataSource.REFERENCE)
@Reference(window=@Window(start=-200,stop=200))


public class GenotypeAndValidateWalker extends RodWalker<GenotypeAndValidateWalker.CountedData, GenotypeAndValidateWalker.CountedData> implements TreeReducible<GenotypeAndValidateWalker.CountedData> {

    /**
     * The optional output file that will have all the variants used in the Genotype and Validation essay.
     */
    @Output(doc="Generate a VCF file with the variants considered by the walker, with a new annotation \"callStatus\" which will carry the value called in the validation VCF or BAM file", required=false)
    protected VCFWriter vcfWriter = null;

    /**
     * The callset to be used as truth (default) or validated (if BAM file is set to truth).
     */
    @Input(fullName="alleles", shortName = "alleles", doc="The set of alleles at which to genotype", required=true)
    public RodBinding<VariantContext> alleles;

    /**
     * Makes the Unified Genotyper calls to the BAM file the truth dataset and validates the alleles ROD binding callset.
     */
    @Argument(fullName ="set_bam_truth", shortName ="bt", doc="Use the calls on the reads (bam file) as the truth dataset and validate the calls on the VCF", required=false)
    private boolean bamIsTruth = false;

    /**
     * The minimum base quality score necessary for a base to be considered when calling a genotype. This argument is passed to the Unified Genotyper.
     */
    @Argument(fullName="minimum_base_quality_score", shortName="mbq", doc="Minimum base quality score for calling a genotype", required=false)
    private int mbq = -1;

    /**
     * The maximum deletion fraction allowed in a site for calling a genotype. This argument is passed to the Unified Genotyper.
     */
    @Argument(fullName="maximum_deletion_fraction", shortName="deletions", doc="Maximum deletion fraction for calling a genotype", required=false)
    private double deletions = -1;

    /**
     * the minimum phred-scaled Qscore threshold to separate high confidence from low confidence calls. This argument is passed to the Unified Genotyper.
     */
    @Argument(fullName="standard_min_confidence_threshold_for_calling", shortName="stand_call_conf", doc="the minimum phred-scaled Qscore threshold to separate high confidence from low confidence calls", required=false)
    private double callConf = -1;

    /**
     * the minimum phred-scaled Qscore threshold to emit low confidence calls. This argument is passed to the Unified Genotyper.
     */
    @Argument(fullName="standard_min_confidence_threshold_for_emitting", shortName="stand_emit_conf", doc="the minimum phred-scaled Qscore threshold to emit low confidence calls", required=false)
    private double emitConf = -1;

    /**
     * Only validate sites that have at least a given depth
     */
    @Argument(fullName="condition_on_depth", shortName="depth", doc="Condition validation on a minimum depth of coverage by the reads", required=false)
    private int minDepth = -1;

    /**
     * If your VCF or BAM file has more than one sample and you only want to validate one, use this parameter to choose it.
     */
    @Hidden
    @Argument(fullName ="sample", shortName ="sn", doc="Name of the sample to validate (in case your VCF/BAM has more than one sample)", required=false)
    private String sample = "";

    /**
     * Print out discordance sites to standard out.
     */
    @Hidden
    @Argument(fullName ="print_interesting_sites", shortName ="print_interesting", doc="Print out interesting sites to standard out", required=false)
    private boolean printInterestingSites;

    private UnifiedGenotyperEngine snpEngine;
    private UnifiedGenotyperEngine indelEngine;

    public static class CountedData {
        private long nAltCalledAlt = 0L;
        private long nAltCalledRef = 0L;
        private long nAltNotCalled = 0L;
        private long nRefCalledAlt = 0L;
        private long nRefCalledRef = 0L;
        private long nRefNotCalled = 0L;
        private long nNoStatusCalledAlt = 0L;
        private long nNoStatusCalledRef = 0L;
        private long nNoStatusNotCalled = 0L;
        private long nNotConfidentCalls = 0L;
        private long nUncovered = 0L;

        /**
         * Adds the values of other to this, returning this
         * @param other the other object
         */
        public void add(CountedData other) {
            nAltCalledAlt += other.nAltCalledAlt;
            nAltCalledRef += other.nAltCalledRef;
            nAltNotCalled += other.nAltNotCalled;
            nRefCalledAlt += other.nRefCalledAlt;
            nRefCalledRef += other.nRefCalledRef;
            nRefNotCalled += other.nRefNotCalled;
            nNoStatusCalledAlt += other.nNoStatusCalledAlt;
            nNoStatusCalledRef += other.nNoStatusCalledRef;
            nNoStatusNotCalled += other.nNoStatusNotCalled;
            nUncovered += other.nUncovered;
            nNotConfidentCalls += other.nNotConfidentCalls;
        }
    }



    //---------------------------------------------------------------------------------------------------------------
    //
    // initialize
    //
    //---------------------------------------------------------------------------------------------------------------

    public void initialize() {

        // Initialize VCF header
        if (vcfWriter != null) {
            Map<String, VCFHeader> header = VCFUtils.getVCFHeadersFromRodPrefix(getToolkit(), alleles.getName());
            Set<String> samples = SampleUtils.getSampleList(header, VariantContextUtils.GenotypeMergeType.REQUIRE_UNIQUE);
            Set<VCFHeaderLine> headerLines = VCFUtils.smartMergeHeaders(header.values(), logger);
            headerLines.add(new VCFHeaderLine("source", "GenotypeAndValidate"));
            vcfWriter.writeHeader(new VCFHeader(headerLines, samples));
        }

        // Filling in SNP calling arguments for UG
        UnifiedArgumentCollection uac = new UnifiedArgumentCollection();
        uac.OutputMode = UnifiedGenotyperEngine.OUTPUT_MODE.EMIT_ALL_SITES;
        uac.alleles = alleles;

        // TODO -- if we change this tool to actually validate against the called allele, then this if statement is needed;
        // TODO -- for now, though, we need to be able to validate the right allele (because we only test isVariant below) [EB]
        //if (!bamIsTruth)
        uac.GenotypingMode = GenotypeLikelihoodsCalculationModel.GENOTYPING_MODE.GENOTYPE_GIVEN_ALLELES;

        if (mbq >= 0) uac.MIN_BASE_QUALTY_SCORE = mbq;
        if (deletions >= 0)
            uac.MAX_DELETION_FRACTION = deletions;
        else
            uac.MAX_DELETION_FRACTION = 1.0;
        if (emitConf >= 0) uac.STANDARD_CONFIDENCE_FOR_EMITTING = emitConf;
        if (callConf >= 0) uac.STANDARD_CONFIDENCE_FOR_CALLING = callConf;

        uac.GLmodel = GenotypeLikelihoodsCalculationModel.Model.SNP;
        snpEngine = new UnifiedGenotyperEngine(getToolkit(), uac);

        // Adding the INDEL calling arguments for UG
        uac.GLmodel = GenotypeLikelihoodsCalculationModel.Model.INDEL;
        indelEngine = new UnifiedGenotyperEngine(getToolkit(), uac);

        // make sure we have callConf set to the threshold set by the UAC so we can use it later.
        callConf = uac.STANDARD_CONFIDENCE_FOR_CALLING;
    }

    //---------------------------------------------------------------------------------------------------------------
    //
    // map
    //
    //---------------------------------------------------------------------------------------------------------------

    public CountedData map( RefMetaDataTracker tracker, ReferenceContext ref, AlignmentContext context ) {

        final CountedData counter = new CountedData();

        // For some reason RodWalkers get map calls with null trackers
        if( tracker == null )
            return counter;

        VariantContext vcComp = tracker.getFirstValue(alleles);
        if( vcComp == null )
            return counter;

        //todo - not sure I want this, may be misleading to filter extended indel events.
        if (isInsideExtendedIndel(vcComp,  ref))
            return counter;

        // Do not operate on variants that are not covered to the optional minimum depth
        if (!context.hasReads() || !context.hasBasePileup() || (minDepth > 0 && context.getBasePileup().getBases().length < minDepth)) {
            counter.nUncovered = 1L;
            if (vcComp.getAttribute("GV").equals("T"))
                counter.nAltNotCalled = 1L;
            else if (vcComp.getAttribute("GV").equals("F"))
                counter.nRefNotCalled = 1L;
            else
                counter.nNoStatusNotCalled = 1L;

            return counter;
        }

        VariantCallContext call;
        if ( vcComp.isSNP() ) {
            call = snpEngine.calculateLikelihoodsAndGenotypes(tracker, ref, context);
        } else if ( vcComp.isIndel() ) {
            call = indelEngine.calculateLikelihoodsAndGenotypes(tracker, ref, context);
        } else if ( bamIsTruth ) {
            // assume it's a SNP if no variation is present; this is necessary so that we can test supposed monomorphic sites against the truth bam
            call = snpEngine.calculateLikelihoodsAndGenotypes(tracker, ref, context);
        } else {
            logger.info("Not SNP or INDEL " + vcComp.getChr() + ":" + vcComp.getStart() + " " + vcComp.getAlleles());
            return counter;
        }


        boolean writeVariant = true;

        if (bamIsTruth) {
            if (call.confidentlyCalled) {
                // If truth is a confident REF call
                if (call.isVariant()) {
                    if (vcComp.isVariant())
                        counter.nAltCalledAlt = 1L;
                    else {
                        counter.nAltCalledRef = 1L;
                        if ( printInterestingSites )
                            System.out.println("Truth=ALT Call=REF at " + call.getChr() + ":" + call.getStart());
                    }
                }
                // If truth is a confident ALT call
                else {
                    if (vcComp.isVariant()) {
                        counter.nRefCalledAlt = 1L;
                        if ( printInterestingSites )
                            System.out.println("Truth=REF Call=ALT at " + call.getChr() + ":" + call.getStart());
                    } else
                        counter.nRefCalledRef = 1L;
                }
            }
            else {
                counter.nNotConfidentCalls = 1L;
                if ( printInterestingSites )
                    System.out.println("Truth is not confident at " + call.getChr() + ":" + call.getStart());
                writeVariant = false;
            }
        }
        else {
//            if (!vcComp.hasAttribute("GV"))
//                throw new UserException.BadInput("Variant has no GV annotation in the INFO field. " + vcComp.getChr() + ":" + vcComp.getStart());

            if (call.isCalledAlt(callConf)) {
                if (vcComp.getAttribute("GV").equals("T"))
                    counter.nAltCalledAlt = 1L;
                else if (vcComp.getAttribute("GV").equals("F")) {
                    counter.nRefCalledAlt = 1L;
                    if ( printInterestingSites )
                        System.out.println("Truth=REF Call=ALT at " + call.getChr() + ":" + call.getStart());
                }
                else
                    counter.nNoStatusCalledAlt = 1L;
            }
            else if (call.isCalledRef(callConf)) {
                if (vcComp.getAttribute("GV").equals("T")) {
                    counter.nAltCalledRef = 1L;
                    if ( printInterestingSites )
                        System.out.println("Truth=ALT Call=REF at " + call.getChr() + ":" + call.getStart());
                }
                else if (vcComp.getAttribute("GV").equals("F"))
                    counter.nRefCalledRef = 1L;

                else
                    counter.nNoStatusCalledRef = 1L;
            }
            else {
                counter.nNotConfidentCalls = 1L;
                if (vcComp.getAttribute("GV").equals("T"))
                    counter.nAltNotCalled = 1L;
                else if (vcComp.getAttribute("GV").equals("F"))
                    counter.nRefNotCalled = 1L;
                else
                    counter.nNoStatusNotCalled = 1L;

                if ( printInterestingSites )
                    System.out.println("Truth is not confident at " + call.getChr() + ":" + call.getStart());
                writeVariant = false;
            }
        }

        if (vcfWriter != null && writeVariant) {
            if (!vcComp.hasAttribute("callStatus")) {
                vcfWriter.add(new VariantContextBuilder(vcComp).attribute("callStatus", call.isCalledAlt(callConf) ? "ALT" : "REF").make());
            }
            else
                vcfWriter.add(vcComp);
        }
        return counter;
    }

    //---------------------------------------------------------------------------------------------------------------
    //
    // reduce
    //
    //---------------------------------------------------------------------------------------------------------------

    public CountedData reduceInit() {
        return new CountedData();
    }

    public CountedData treeReduce( final CountedData sum1, final CountedData sum2) {
        sum2.add(sum1);
        return sum2;
    }

    public CountedData reduce( final CountedData mapValue, final CountedData reduceSum ) {
        reduceSum.add(mapValue);
        return reduceSum;
    }

    public void onTraversalDone( CountedData reduceSum ) {
        double ppv = 100 * ((double) reduceSum.nAltCalledAlt /( reduceSum.nAltCalledAlt + reduceSum.nRefCalledAlt));
        double npv = 100 * ((double) reduceSum.nRefCalledRef /( reduceSum.nRefCalledRef + reduceSum.nAltCalledRef));
        double sensitivity = 100 * ((double) reduceSum.nAltCalledAlt /( reduceSum.nAltCalledAlt + reduceSum.nAltCalledRef));
        double specificity = (reduceSum.nRefCalledRef + reduceSum.nRefCalledAlt > 0) ? 100 * ((double) reduceSum.nRefCalledRef /( reduceSum.nRefCalledRef + reduceSum.nRefCalledAlt)) : 100;
        logger.info(String.format("Resulting Truth Table Output\n\n" +
                                  "------------------------------------------------------------------\n" +
                                  "\t\t|\tALT\t|\tREF\t|\tNo Status\n"  +
                                  "------------------------------------------------------------------\n" +
                                  "called alt\t|\t%d\t|\t%d\t|\t%d\n" +
                                  "called ref\t|\t%d\t|\t%d\t|\t%d\n" +
                                  "not called\t|\t%d\t|\t%d\t|\t%d\n" +
                                  "------------------------------------------------------------------\n" +
                                  "positive predictive value: %f%%\n" +
                                  "negative predictive value: %f%%\n" +
                                  "------------------------------------------------------------------\n" +
                                  "sensitivity: %f%%\n" +
                                  "specificity: %f%%\n" +
                                  "------------------------------------------------------------------\n" +
                                  "not confident: %d\n" +
                                  "not covered: %d\n" +
                                  "------------------------------------------------------------------\n", reduceSum.nAltCalledAlt, reduceSum.nRefCalledAlt, reduceSum.nNoStatusCalledAlt, reduceSum.nAltCalledRef, reduceSum.nRefCalledRef, reduceSum.nNoStatusCalledRef, reduceSum.nAltNotCalled, reduceSum.nRefNotCalled, reduceSum.nNoStatusNotCalled, ppv, npv, sensitivity, specificity, reduceSum.nNotConfidentCalls, reduceSum.nUncovered));
    }
}
