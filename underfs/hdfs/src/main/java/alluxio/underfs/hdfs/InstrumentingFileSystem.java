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

package alluxio.underfs.hdfs;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.util.Progressable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

public class InstrumentingFileSystem extends FileSystem {
  private static final Logger LOG = LoggerFactory.getLogger(InstrumentingFileSystem.class);

  private FileSystem mFileSystem;

  public InstrumentingFileSystem(FileSystem fileSystem) {
    mFileSystem = fileSystem;
  }

  @Override
  public URI getUri() {
    return mFileSystem.getUri();
  }

  @Override
  public FSDataInputStream open(Path path, int i) throws IOException {
    return mFileSystem.open(path, i);
  }

  @Override
  public FSDataOutputStream create(Path path, FsPermission fsPermission, boolean b, int i,
                                   short i1, long l, Progressable progressable)
      throws IOException {
    return mFileSystem.create(path, fsPermission, b, i, i1, l, progressable);
  }

  @Override
  public FSDataOutputStream append(Path path, int i, Progressable progressable) throws IOException {
    return mFileSystem.append(path, i, progressable);
  }

  @Override
  public void setWorkingDirectory(Path path) {
    mFileSystem.setWorkingDirectory(path);
  }

  @Override
  public Path getWorkingDirectory() {
    return mFileSystem.getWorkingDirectory();
  }

  @Override
  public boolean mkdirs(Path path, FsPermission fsPermission) throws IOException {
    return mFileSystem.mkdirs(path, fsPermission);
  }

  @Override
  public BlockLocation[] getFileBlockLocations(Path p, long start, long len) throws IOException {
    LOG.info(String.format("getFileBlockLocations: %s %i %i", p.toString(), start, len));
    return super.getFileBlockLocations(p, start, len);
  }

  @Override
  public FSDataInputStream open(Path f) throws IOException {
    LOG.info(String.format("open: %s", f.toString()));
    return super.open(f);
  }

  @Override
  public FSDataOutputStream create(Path f) throws IOException {
    LOG.info(String.format("create: %s", f.toString()));
    return super.create(f);
  }

  @Override
  public boolean rename(Path path, Path path1) throws IOException {
    LOG.info(String.format("rename: %s %s", path.toString(), path1.toString()));
    return mFileSystem.rename(path, path1);
  }

  @Override
  public boolean delete(Path path, boolean b) throws IOException {
    LOG.info(String.format("delete: %s %i", path.toString(), b));
    return mFileSystem.delete(path, b);
  }

  @Override
  public FileStatus getFileStatus(Path path) throws IOException {
    LOG.info(String.format("getFileStatus: %s", path.toString()));
    return mFileSystem.getFileStatus(path);
  }

  @Override
  public FileStatus[] listStatus(Path path) throws FileNotFoundException, IOException {
    LOG.info(String.format("listStatus: %s", path.toString()));
    return mFileSystem.listStatus(path);
  }

  @Override
  public boolean mkdirs(Path f) throws IOException {
    LOG.info(String.format("mkdirs: %s", f.toString()));
    return super.mkdirs(f);
  }
}
