package alluxio.job.plan.transform.format.orc;

import alluxio.AlluxioURI;
import alluxio.job.plan.transform.PartitionInfo;
import alluxio.job.plan.transform.format.JobPath;
import alluxio.job.plan.transform.format.ReadWriterUtils;
import alluxio.job.plan.transform.format.TableReader;
import alluxio.job.plan.transform.format.TableRow;
import alluxio.job.plan.transform.format.TableSchema;

import alluxio.job.plan.transform.format.csv.CsvReader;
import com.google.common.io.Closer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.orc.RecordReader;

import java.io.IOException;
import java.util.List;

public final class OrcReader implements TableReader {

  private final Closer mCloser;
  private final OrcSchema mSchema;
  private final Reader mReader;
  private final RecordReader mRows;
  private final List<String> mFieldNames;

  private VectorizedRowBatch mCurrentBatch;
  private int mCurrentBatchPosition;

  private OrcReader(JobPath inputPath, PartitionInfo pInfo) throws IOException {
    mCloser = Closer.create();
    try {
      mSchema = new OrcSchema(pInfo.getFields());

      Configuration conf = ReadWriterUtils.readNoCacheConf();

      mReader = mCloser.register(OrcFile.createReader(inputPath, OrcFile.readerOptions(conf));
      mFieldNames = mReader.getSchema().getFieldNames();
      mRows = mReader.rows();
    } catch (IOException e) {
      try {
        mCloser.close();
      } catch (IOException ioe) {
        e.addSuppressed(ioe);
      }
      throw e;
    }
  }

  public static OrcReader create(AlluxioURI uri, PartitionInfo pInfo) throws IOException {
    JobPath path = new JobPath(uri.getScheme(), uri.getAuthority().toString(), uri.getPath());
    return new OrcReader(path, pInfo);
  }

  @Override
  public TableSchema getSchema() {
    return mSchema;
  }

  @Override
  public TableRow read() throws IOException {
    mCurrentBatch = mReader.getSchema().createRowBatch();
    mCurrentBatchPosition = 0;
    if (!mRows.nextBatch(mCurrentBatch)) {
      return null;
    }

    return new OrcRow(mSchema, mCurrentBatch, mCurrentBatchPosition, mFieldNames);
  }

  @Override
  public void close() throws IOException {
    mCloser.close();
  }
}
