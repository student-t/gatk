package org.broadinstitute.sting.utils.recalibration.covariates;

import org.broadinstitute.sting.utils.recalibration.ReadCovariates;
import org.broadinstitute.sting.gatk.walkers.bqsr.RecalibrationArgumentCollection;
import org.broadinstitute.sting.utils.exceptions.UserException;
import org.broadinstitute.sting.utils.sam.GATKSAMRecord;

/**
 * Binary covariate allows BQSR to recalibrate based on a binary covariate in the BAM file. This covariate should assume values of 1 and 0.
 *
 * Private (test) covariate that shouldn't be used by the general public
 *
 * @author Mauricio Carneiro
 * @since 7/6/12
 */
public class BinaryTagCovariate implements ExperimentalCovariate {

    private String tag;

    @Override
    public void initialize(RecalibrationArgumentCollection RAC) {
        if ( RAC.BINARY_TAG_NAME == null )
            throw new UserException.BadArgumentValue("bintag", "no name provided for binary tag covariate");
        tag = RAC.BINARY_TAG_NAME;
    }

    @Override
    public void recordValues(GATKSAMRecord read, ReadCovariates values) {
        final Object tagObject = read.getAttribute(tag);

        byte[] binaryTag;
        if (tagObject instanceof byte[])
            binaryTag = (byte[]) tagObject;
        else if (tagObject instanceof String) {
            int readLength = ((String) tagObject).length();
            binaryTag = new byte[readLength];
            for (int i = 0; i<readLength; i++)
                binaryTag[i] = Byte.decode(((String) tagObject).substring(i, i+1));
        }
        else
            throw new UserException("Binary tag is not a byte array (fast) or a string (slow). Type not supported");

        for (int i = 0; i < read.getReadLength(); i++) {
            values.addCovariate((int) binaryTag[i], (int) binaryTag[i], (int) binaryTag[i], i);
        }
    }

    @Override
    public Object getValue(String str) {
        return Integer.decode(str);
    }

    @Override
    public String formatKey(int key) {
        return String.format("%d", key);
    }

    @Override
    public int keyFromValue(Object value) {
        return Integer.decode((String) value);
    }

    @Override
    public int maximumKeyValue() {
        return 1;
    }
}
