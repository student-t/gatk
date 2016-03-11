/*
* By downloading the PROGRAM you agree to the following terms of use:
* 
* BROAD INSTITUTE
* SOFTWARE LICENSE AGREEMENT
* FOR ACADEMIC NON-COMMERCIAL RESEARCH PURPOSES ONLY
* 
* This Agreement is made between the Broad Institute, Inc. with a principal address at 415 Main Street, Cambridge, MA 02142 ("BROAD") and the LICENSEE and is effective at the date the downloading is completed ("EFFECTIVE DATE").
* 
* WHEREAS, LICENSEE desires to license the PROGRAM, as defined hereinafter, and BROAD wishes to have this PROGRAM utilized in the public interest, subject only to the royalty-free, nonexclusive, nontransferable license rights of the United States Government pursuant to 48 CFR 52.227-14; and
* WHEREAS, LICENSEE desires to license the PROGRAM and BROAD desires to grant a license on the following terms and conditions.
* NOW, THEREFORE, in consideration of the promises and covenants made herein, the parties hereto agree as follows:
* 
* 1. DEFINITIONS
* 1.1 PROGRAM shall mean copyright in the object code and source code known as GATK3 and related documentation, if any, as they exist on the EFFECTIVE DATE and can be downloaded from http://www.broadinstitute.org/gatk on the EFFECTIVE DATE.
* 
* 2. LICENSE
* 2.1 Grant. Subject to the terms of this Agreement, BROAD hereby grants to LICENSEE, solely for academic non-commercial research purposes, a non-exclusive, non-transferable license to: (a) download, execute and display the PROGRAM and (b) create bug fixes and modify the PROGRAM. LICENSEE hereby automatically grants to BROAD a non-exclusive, royalty-free, irrevocable license to any LICENSEE bug fixes or modifications to the PROGRAM with unlimited rights to sublicense and/or distribute.  LICENSEE agrees to provide any such modifications and bug fixes to BROAD promptly upon their creation.
* The LICENSEE may apply the PROGRAM in a pipeline to data owned by users other than the LICENSEE and provide these users the results of the PROGRAM provided LICENSEE does so for academic non-commercial purposes only. For clarification purposes, academic sponsored research is not a commercial use under the terms of this Agreement.
* 2.2 No Sublicensing or Additional Rights. LICENSEE shall not sublicense or distribute the PROGRAM, in whole or in part, without prior written permission from BROAD. LICENSEE shall ensure that all of its users agree to the terms of this Agreement. LICENSEE further agrees that it shall not put the PROGRAM on a network, server, or other similar technology that may be accessed by anyone other than the LICENSEE and its employees and users who have agreed to the terms of this agreement.
* 2.3 License Limitations. Nothing in this Agreement shall be construed to confer any rights upon LICENSEE by implication, estoppel, or otherwise to any computer software, trademark, intellectual property, or patent rights of BROAD, or of any other entity, except as expressly granted herein. LICENSEE agrees that the PROGRAM, in whole or part, shall not be used for any commercial purpose, including without limitation, as the basis of a commercial software or hardware product or to provide services. LICENSEE further agrees that the PROGRAM shall not be copied or otherwise adapted in order to circumvent the need for obtaining a license for use of the PROGRAM.
* 
* 3. PHONE-HOME FEATURE
* LICENSEE expressly acknowledges that the PROGRAM contains an embedded automatic reporting system ("PHONE-HOME") which is enabled by default upon download. Unless LICENSEE requests disablement of PHONE-HOME, LICENSEE agrees that BROAD may collect limited information transmitted by PHONE-HOME regarding LICENSEE and its use of the PROGRAM.  Such information shall include LICENSEE’S user identification, version number of the PROGRAM and tools being run, mode of analysis employed, and any error reports generated during run-time.  Collection of such information is used by BROAD solely to monitor usage rates, fulfill reporting requirements to BROAD funding agencies, drive improvements to the PROGRAM, and facilitate adjustments to PROGRAM-related documentation.
* 
* 4. OWNERSHIP OF INTELLECTUAL PROPERTY
* LICENSEE acknowledges that title to the PROGRAM shall remain with BROAD. The PROGRAM is marked with the following BROAD copyright notice and notice of attribution to contributors. LICENSEE shall retain such notice on all copies. LICENSEE agrees to include appropriate attribution if any results obtained from use of the PROGRAM are included in any publication.
* Copyright 2012-2016 Broad Institute, Inc.
* Notice of attribution: The GATK3 program was made available through the generosity of Medical and Population Genetics program at the Broad Institute, Inc.
* LICENSEE shall not use any trademark or trade name of BROAD, or any variation, adaptation, or abbreviation, of such marks or trade names, or any names of officers, faculty, students, employees, or agents of BROAD except as states above for attribution purposes.
* 
* 5. INDEMNIFICATION
* LICENSEE shall indemnify, defend, and hold harmless BROAD, and their respective officers, faculty, students, employees, associated investigators and agents, and their respective successors, heirs and assigns, (Indemnitees), against any liability, damage, loss, or expense (including reasonable attorneys fees and expenses) incurred by or imposed upon any of the Indemnitees in connection with any claims, suits, actions, demands or judgments arising out of any theory of liability (including, without limitation, actions in the form of tort, warranty, or strict liability and regardless of whether such action has any factual basis) pursuant to any right or license granted under this Agreement.
* 
* 6. NO REPRESENTATIONS OR WARRANTIES
* THE PROGRAM IS DELIVERED AS IS. BROAD MAKES NO REPRESENTATIONS OR WARRANTIES OF ANY KIND CONCERNING THE PROGRAM OR THE COPYRIGHT, EXPRESS OR IMPLIED, INCLUDING, WITHOUT LIMITATION, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NONINFRINGEMENT, OR THE ABSENCE OF LATENT OR OTHER DEFECTS, WHETHER OR NOT DISCOVERABLE. BROAD EXTENDS NO WARRANTIES OF ANY KIND AS TO PROGRAM CONFORMITY WITH WHATEVER USER MANUALS OR OTHER LITERATURE MAY BE ISSUED FROM TIME TO TIME.
* IN NO EVENT SHALL BROAD OR ITS RESPECTIVE DIRECTORS, OFFICERS, EMPLOYEES, AFFILIATED INVESTIGATORS AND AFFILIATES BE LIABLE FOR INCIDENTAL OR CONSEQUENTIAL DAMAGES OF ANY KIND, INCLUDING, WITHOUT LIMITATION, ECONOMIC DAMAGES OR INJURY TO PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER BROAD SHALL BE ADVISED, SHALL HAVE OTHER REASON TO KNOW, OR IN FACT SHALL KNOW OF THE POSSIBILITY OF THE FOREGOING.
* 
* 7. ASSIGNMENT
* This Agreement is personal to LICENSEE and any rights or obligations assigned by LICENSEE without the prior written consent of BROAD shall be null and void.
* 
* 8. MISCELLANEOUS
* 8.1 Export Control. LICENSEE gives assurance that it will comply with all United States export control laws and regulations controlling the export of the PROGRAM, including, without limitation, all Export Administration Regulations of the United States Department of Commerce. Among other things, these laws and regulations prohibit, or require a license for, the export of certain types of software to specified countries.
* 8.2 Termination. LICENSEE shall have the right to terminate this Agreement for any reason upon prior written notice to BROAD. If LICENSEE breaches any provision hereunder, and fails to cure such breach within thirty (30) days, BROAD may terminate this Agreement immediately. Upon termination, LICENSEE shall provide BROAD with written assurance that the original and all copies of the PROGRAM have been destroyed, except that, upon prior written authorization from BROAD, LICENSEE may retain a copy for archive purposes.
* 8.3 Survival. The following provisions shall survive the expiration or termination of this Agreement: Articles 1, 3, 4, 5 and Sections 2.2, 2.3, 7.3, and 7.4.
* 8.4 Notice. Any notices under this Agreement shall be in writing, shall specifically refer to this Agreement, and shall be sent by hand, recognized national overnight courier, confirmed facsimile transmission, confirmed electronic mail, or registered or certified mail, postage prepaid, return receipt requested. All notices under this Agreement shall be deemed effective upon receipt.
* 8.5 Amendment and Waiver; Entire Agreement. This Agreement may be amended, supplemented, or otherwise modified only by means of a written instrument signed by all parties. Any waiver of any rights or failure to act in a specific instance shall relate only to such instance and shall not be construed as an agreement to waive any rights or fail to act in any other instance, whether or not similar. This Agreement constitutes the entire agreement among the parties with respect to its subject matter and supersedes prior agreements or understandings between the parties relating to its subject matter.
* 8.6 Binding Effect; Headings. This Agreement shall be binding upon and inure to the benefit of the parties and their respective permitted successors and assigns. All headings are for convenience only and shall not affect the meaning of any provision of this Agreement.
* 8.7 Governing Law. This Agreement shall be construed, governed, interpreted and applied in accordance with the internal laws of the Commonwealth of Massachusetts, U.S.A., without regard to conflict of laws principles.
*/

package org.broadinstitute.gatk.tools.walkers.diagnostics;

import com.sun.org.apache.xpath.internal.operations.Bool;
import htsjdk.samtools.SAMReadGroupRecord;
import org.broadinstitute.gatk.utils.commandline.Argument;
import org.broadinstitute.gatk.utils.commandline.ArgumentCollection;
import org.broadinstitute.gatk.utils.commandline.Gather;
import org.broadinstitute.gatk.utils.commandline.Output;
import org.broadinstitute.gatk.engine.CommandLineGATK;
import org.broadinstitute.gatk.engine.arguments.DbsnpArgumentCollection;
import org.broadinstitute.gatk.utils.contexts.AlignmentContext;
import org.broadinstitute.gatk.utils.contexts.ReferenceContext;
import org.broadinstitute.gatk.utils.refdata.RefMetaDataTracker;
import org.broadinstitute.gatk.utils.report.GATKReport;
import org.broadinstitute.gatk.utils.report.GATKReportGatherer;
import org.broadinstitute.gatk.utils.report.GATKReportTable;
import org.broadinstitute.gatk.engine.walkers.LocusWalker;
import org.broadinstitute.gatk.engine.walkers.PartitionBy;
import org.broadinstitute.gatk.engine.walkers.PartitionType;
import org.broadinstitute.gatk.utils.BaseUtils;
import org.broadinstitute.gatk.utils.GenomeLoc;
import org.broadinstitute.gatk.utils.collections.Pair;
import org.broadinstitute.gatk.utils.pileup.ReadBackedPileup;
import htsjdk.variant.variantcontext.VariantContext;
import org.broadinstitute.gatk.utils.help.HelpConstants;
import org.broadinstitute.gatk.utils.help.DocumentedGATKFeature;

import java.io.PrintStream;
import java.lang.Boolean;
import java.util.*;

/**
 * Calculates coverage and GC content for each interval
 *
 * <p/>
 * <p>Can be stratified per sample or per read group.</p>
 * <p/>
 * <h3>Inputs</h3>
 * <p>
 * <ul>
 * <li>A reference file</li>
 * <li>A bam file, multiple bams, or a list of bams</li>
 * </p>
 * <p/>
 * <h3>Output</h3>
 * <p>
 * GATKReport (type of tab-delimited text file) showing average coverage per read group per interval. Stratified per sample or per read group.
 * </p>
 * <p/>
 * <h3>Examples</h3>
 * <h4>Report results per sample</h4>
 * <pre>
 * java -jar GenomeAnalysisTK.jar \
 *   -R reference.fasta \
 *   -T CoverageByIntervalWithGC \
 *   -I input.bam \
 *   -o output.txt \
 *   -L input.intervals
 * </pre>
 * <h4>Report results per read group</h4>
 * <pre>
 * java -jar GenomeAnalysisTK.jar \
 *   -R reference.fasta \
 *   -T CoverageByIntervalWithGC \
 *   -I input.bam \
 *   -byRG \
 *   -o output.txt \
 *   -L input.intervals
 * </pre>
 */
@DocumentedGATKFeature( groupName = HelpConstants.DOCS_CAT_QC, extraDocs = {CommandLineGATK.class} )
@PartitionBy(PartitionType.INTERVAL)
public class CoverageByIntervalWithGC extends LocusWalker<LinkedHashMap<String, Long>, LinkedHashMap<String, Long>> {

    @Output
    @Gather(GATKReportGatherer.class)
    PrintStream out;

    /**
     * Report the results per read group instead of per sample.
     */
    @Argument(fullName = "byReadGroup", shortName = "byRG", doc = "Stratify by readgroup", required = false)
    private Boolean byRG = false; // TODO: not used yet

    @ArgumentCollection
    protected DbsnpArgumentCollection dbsnp = new DbsnpArgumentCollection();

    GATKReportTable reportTable;

    final String columnInterval = "Interval";
    final String columnGC = "GCContent";
    final String columnIntervalSize = "IntervalSize";
    final String columnSNPs = "SNPs";
    final String columnIndels = "Indels";

    private List<String> unitIDs = new LinkedList<String>(); // generic for samples and readgroups

    public void initialize() {

        String unitType = "sample";

        for (SAMReadGroupRecord RG : getToolkit().getSAMFileHeader().getReadGroups()) {

            if (byRG) {
                String readGroupID = RG.getReadGroupId();
                unitIDs.add(readGroupID);
                unitType = "read group";
            } else {
                String sampleID = RG.getSample();
                if (!unitIDs.contains(sampleID)) {
                    unitIDs.add(sampleID);
                }
                unitType = "sample";
            }
        }

        //Sets up our report table columns (by Read Groups + GCcontent + variant counts)
        reportTable = new GATKReportTable("CoverageByIntervalWithGC", "A table with the coverage per interval for each " + unitType, 4 + unitIDs.size());

        reportTable.addColumn(columnInterval);
        reportTable.addColumn(columnGC);
        reportTable.addColumn(columnIntervalSize);
        reportTable.addColumn(columnSNPs);
        reportTable.addColumn(columnIndels);

        for (String unitID : unitIDs) {
            reportTable.addColumn(unitID);
        }
    }

    public boolean isReduceByInterval() {
        // onTraversalDone is called after every interval
        return true;
    }

    @Override
    public LinkedHashMap<String, Long> map(RefMetaDataTracker tracker, ReferenceContext ref, AlignmentContext context) {
        LinkedHashMap<String, Long> output = new LinkedHashMap<String, Long>();

        VariantContext variantContext = tracker.getFirstValue(dbsnp.dbsnp, ref.getLocus());
        if (variantContext == null) {
            output.put(columnSNPs, 0L);
            output.put(columnIndels, 0L);
        } else if (variantContext.getType() == VariantContext.Type.SNP) {
            output.put(columnSNPs, 1L);
        } else if (variantContext.getType() == VariantContext.Type.INDEL) {
            output.put(columnIndels, 1L);
        }
        // TODO: Maybe handle other types of variants? At least MIXED? There's probably not enough of them to matter though.

        byte base = ref.getBase();                              // Update site GC content for interval
        output.put(columnGC, (base == BaseUtils.Base.G.base || base == BaseUtils.Base.C.base) ? 1L : 0L);

        ReadBackedPileup pileup = context.getBasePileup();      // Update site pileup for all groups of read groups

        for (String unitID : unitIDs) {
            if (byRG) {
                ReadBackedPileup rgPileup = pileup.getPileupForReadGroup(unitID);
                output.put(unitID, (rgPileup == null) ? 0L : (long) rgPileup.depthOfCoverage());
            } else {
                ReadBackedPileup rgPileup = pileup.getPileupForSample(unitID);
                output.put(unitID, (rgPileup == null) ? 0L : (long) rgPileup.depthOfCoverage());
            }
        }

        return output;
    }

    @Override
    public LinkedHashMap<String, Long> reduceInit() {
        return new LinkedHashMap<String, Long>();

    }

    @Override
    public LinkedHashMap<String, Long> reduce(LinkedHashMap<String, Long> value, LinkedHashMap<String, Long> sum) {
        for (String key : value.keySet()) {
            if (sum.containsKey(key))
                sum.put(key, value.get(key) + sum.get(key));  // sum all keys (if they exist in the sum object).
            else
                sum.put(key, value.get(key));                 // if they don't, just put the first one.

        }

        return sum;
    }

    @Override
    public void onTraversalDone(List<Pair<GenomeLoc, LinkedHashMap<String, Long>>> results) {
        int rowID = 0;
        for (Pair<GenomeLoc, LinkedHashMap<String, Long>> intervalPair : results) {
            GenomeLoc interval = intervalPair.getFirst();
            LinkedHashMap<String, Long> counts = intervalPair.getSecond();
            rowID++;
            reportTable.set(rowID, columnInterval, interval.toString());

            // Get coverage by taking total counts and dividing by interval length
            for (String key : counts.keySet())
                if (!key.equals(columnSNPs) && !key.equals(columnIndels))
                    reportTable.set(rowID, key, (double) counts.get(key) / (double) interval.size());

            reportTable.set(rowID, columnIntervalSize, interval.size());

            if (counts.containsKey(columnSNPs))
                reportTable.set(rowID, columnSNPs, counts.get(columnSNPs));

            if (counts.containsKey(columnIndels))
                reportTable.set(rowID, columnIndels, counts.get(columnIndels));
        }
        GATKReport report = new GATKReport(reportTable);

        report.print(out);
    }
}
