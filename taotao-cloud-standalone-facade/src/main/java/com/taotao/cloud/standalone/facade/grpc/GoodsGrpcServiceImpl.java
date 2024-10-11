package com.taotao.cloud.standalone.facade.grpc;

import com.taotao.cloud.standalone.api.grpc.CountStoreGoodsNumGrpcRequest;
import com.taotao.cloud.standalone.api.grpc.CountStoreGoodsNumGrpcResponse;
import com.taotao.cloud.standalone.api.grpc.GoodsGrpcServiceGrpc.GoodsGrpcServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.lognet.springboot.grpc.GRpcService;

@GrpcService
public class GoodsGrpcServiceImpl extends GoodsGrpcServiceImplBase {

	@Override
	public void countStoreGoodsNum(CountStoreGoodsNumGrpcRequest request,
		StreamObserver<CountStoreGoodsNumGrpcResponse> responseObserver) {
		super.countStoreGoodsNum(request, responseObserver);
	}
}
