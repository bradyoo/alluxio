// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc/catalog/catalog_master.proto

package alluxio.grpc.catalog;

public interface RangeSetOrBuilder extends
    // @@protoc_insertion_point(interface_extends:alluxio.grpc.catalog.RangeSet)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .alluxio.grpc.catalog.Range ranges = 1;</code>
   */
  java.util.List<alluxio.grpc.catalog.Range> 
      getRangesList();
  /**
   * <code>repeated .alluxio.grpc.catalog.Range ranges = 1;</code>
   */
  alluxio.grpc.catalog.Range getRanges(int index);
  /**
   * <code>repeated .alluxio.grpc.catalog.Range ranges = 1;</code>
   */
  int getRangesCount();
  /**
   * <code>repeated .alluxio.grpc.catalog.Range ranges = 1;</code>
   */
  java.util.List<? extends alluxio.grpc.catalog.RangeOrBuilder> 
      getRangesOrBuilderList();
  /**
   * <code>repeated .alluxio.grpc.catalog.Range ranges = 1;</code>
   */
  alluxio.grpc.catalog.RangeOrBuilder getRangesOrBuilder(
      int index);
}