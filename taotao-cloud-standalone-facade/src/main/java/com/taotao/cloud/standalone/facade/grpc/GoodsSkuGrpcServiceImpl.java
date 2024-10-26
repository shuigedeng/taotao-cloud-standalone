/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.standalone.facade.grpc;

import com.taotao.cloud.standalone.api.grpc.GoodsSkuGrpcRequest;
import com.taotao.cloud.standalone.api.grpc.GoodsSkuGrpcResponse;
import com.taotao.cloud.standalone.api.grpc.GoodsSkuGrpcServiceGrpc.GoodsSkuGrpcServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class GoodsSkuGrpcServiceImpl extends GoodsSkuGrpcServiceImplBase {

    @Override
    public void updateBatchById(GoodsSkuGrpcRequest request, StreamObserver<GoodsSkuGrpcResponse> responseObserver) {
        super.updateBatchById(request, responseObserver);
    }

    @Override
    public void updateGoodsStuck(GoodsSkuGrpcRequest request, StreamObserver<GoodsSkuGrpcResponse> responseObserver) {
        super.updateGoodsStuck(request, responseObserver);
    }

    @Override
    public void getGoodsSkuByIdFromCache(
            GoodsSkuGrpcRequest request, StreamObserver<GoodsSkuGrpcResponse> responseObserver) {
        super.getGoodsSkuByIdFromCache(request, responseObserver);
    }
}
