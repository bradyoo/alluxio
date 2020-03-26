/*
 * The Alluxio Open Foundation licenses this work under the Apache License, version 2.0
 * (the "License"). You may not use this work except in compliance with the License, which is
 * available at www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied, as more fully set forth in the License.
 *
 * See the NOTICE file distributed with this work for information regarding copyright ownership.
 */


package alluxio.job.plan.transform.format.orc;

import alluxio.job.plan.transform.FieldSchema;
import alluxio.job.plan.transform.format.SchemaConversionUtils;
import alluxio.job.plan.transform.format.TableSchema;
import alluxio.job.plan.transform.format.parquet.ParquetSchema;
import org.apache.avro.Schema;
import org.apache.orc.Reader;
import org.apache.orc.TypeDescription;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrcSchema implements TableSchema {
  private final ArrayList<FieldSchema> mAlluxioSchema;

  private final Schema mWriteSchema;

  public OrcSchema(Reader reader) throws IOException {
    mAlluxioSchema = new ArrayList<>();

    final List<String> fieldNames = reader.getSchema().getFieldNames();
    for (int i = 0; i < fieldNames.size(); i++) {
      final String fieldName = fieldNames.get(i);

      final String type = getType(reader.getSchema().getChildren().get(i));

      mAlluxioSchema.add(new FieldSchema(i, fieldName, type, ""));
    }

    mWriteSchema = SchemaConversionUtils.buildWriteSchema(Schema.Type.RECORD.getName(), mAlluxioSchema);
  }
  
  private String getType(TypeDescription typeDescription) {
    final TypeDescription.Category category = typeDescription.getCategory();
    switch (category) {
      case DECIMAL:
        return String.format("decimal(%d,%d)", typeDescription.getPrecision(),
            typeDescription.getScale());
      case CHAR:
      case VARCHAR:
        return String.format("%s(%d)", category.getName(), typeDescription.getMaxLength());
      default:
        return category.getName();
    }
  }  

  @Override
  public ParquetSchema toParquet() {
    return new ParquetSchema(mWriteSchema);
  }

  public ArrayList<FieldSchema> getAlluxioSchema() {
    return mAlluxioSchema;
  }

  public Schema getWriteSchema() {
    return mWriteSchema;
  }
}
