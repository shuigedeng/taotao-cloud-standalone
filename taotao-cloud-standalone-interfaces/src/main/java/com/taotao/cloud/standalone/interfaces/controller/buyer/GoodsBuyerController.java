package com.taotao.cloud.standalone.interfaces.controller.buyer; /// *
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package com.taotao.cloud.standalone.facade.controller.buyer;
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.taotao.boot.common.model.PageResult;
// import com.taotao.boot.common.model.Result;
// import com.taotao.cloud.data.mybatis.mybatisplus.MpUtils;
// import com.taotao.cloud.standalone.application.command.standalone.dto.clientobject.standaloneCO;
// import com.taotao.cloud.standalone.application.command.standalone.dto.clientobject.standaloneSkuParamsCO;
// import com.taotao.cloud.standalone.application.elasticsearch.entity.EsGoodsIndex;
// import com.taotao.cloud.standalone.application.elasticsearch.pojo.EsGoodsRelatedInfo;
// import com.taotao.cloud.standalone.application.service.IEsGoodsSearchService;
// import com.taotao.cloud.standalone.application.service.IGoodsService;
// import com.taotao.cloud.standalone.application.service.IGoodsSkuService;
// import com.taotao.boot.web.request.annotation.RequestLogger;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.Parameters;
// import io.swagger.v3.oas.annotations.enums.ParameterIn;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.constraints.NotNull;
// import java.util.List;
// import java.util.Map;
// import lombok.AllArgsConstructor;
// import org.springframework.data.elasticsearch.core.SearchPage;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
/// **
// * 买家端,商品接口
// *
// * @author shuigedeng
// * @version 2022.04
// * @since 2022-04-20 16:59:38
// */
// @AllArgsConstructor
// @Validated
// @RestController
// @Tag(name = "买家端-商品API", description = "买家端-商品API")
// @RequestMapping("/goods/buyer/goods")
// public class GoodsBuyerController extends BusinessController {
//
//	/**
//	 * 商品
//	 */
//	private final IGoodsService goodsService;
//	/**
//	 * 商品SKU
//	 */
//	private final IGoodsSkuService goodsSkuService;
//	/**
//	 * ES商品搜索
//	 */
//	private final IEsGoodsSearchService goodsSearchService;
//
//	@Operation(summary = "通过id获取商品信息", description = "通过id获取商品信息")
//	@Parameters({
//		@Parameter(name = "goodsId", required = true, description = "商品ID", in = ParameterIn.PATH),
//	})
//	@RequestLogger
//	@PreAuthorize("hasAuthority('dept:tree:data')")
//	@GetMapping(value = "/{goodsId}")
//	public Result<GoodsSkuParamsCO> get(
//		@NotNull(message = "商品ID不能为空") @PathVariable Long goodsId) {
//		return Result.success(goodsService.getGoodsVO(goodsId));
//	}
//
//	@Operation(summary = "通过skuId获取商品信息", description = "通过skuId获取商品信息")
//	@Parameters({
//		@Parameter(name = "goodsId", required = true, description = "商品ID", in = ParameterIn.PATH),
//		@Parameter(name = "skuId", required = true, description = "skuId", in = ParameterIn.PATH),
//	})
//	@RequestLogger
//	@PreAuthorize("hasAuthority('dept:tree:data')")
//	@GetMapping(value = "/{goodsId}/{skuId}")
//	// @PageViewPoint(type = PageViewEnum.SKU, id = "#id")
//	public Result<Map<String, Object>> getSku(
//		@NotNull(message = "商品ID不能为空") @PathVariable Long goodsId,
//		@NotNull(message = "skuId不能为空") @PathVariable Long skuId) {
//		Map<String, Object> map = goodsSkuService.getGoodsSkuDetail(goodsId, skuId);
//		return Result.success(map);
//	}
//
//	@Operation(summary = "获取商品分页列表", description = "获取商品分页列表")
//	@RequestLogger
//	@PreAuthorize("hasAuthority('dept:tree:data')")
//	@GetMapping("/page")
//	public Result<PageResult<GoodsCO>> getByPage(@Validated GoodsPageQuery goodsPageQuery) {
//		IPage<Goods> goodsPage = goodsService.standaloneQueryPage(goodsPageQuery);
//		return Result.success(MpUtils.convertMybatisPage(goodsPage, GoodsCO.class));
//	}
//
//	@Operation(summary = "从ES中获取商品信息", description = "从ES中获取商品信息")
//	@RequestLogger
//	@GetMapping("/es")
//	public Result<SearchPage<EsGoodsIndex>> getGoodsByPageFromEs(
//		@Validated EsGoodsSearchQuery goodsSearchParams) {
//		SearchPage<EsGoodsIndex> esGoodsIndices = goodsSearchService.searchGoods(goodsSearchParams);
//		return Result.success(esGoodsIndices);
//	}
//
//	@Operation(summary = "从ES中获取相关商品品牌名称，分类名称及属性", description = "从ES中获取相关商品品牌名称，分类名称及属性")
//	@RequestLogger
//	@PreAuthorize("hasAuthority('dept:tree:data')")
//	@GetMapping("/es/related")
//	public Result<EsGoodsRelatedInfo> getGoodsRelatedByPageFromEs(
//		@Validated EsGoodsSearchQuery esGoodsSearchQuery) {
//		// pageVO.setNotConvert(true);
//		EsGoodsRelatedInfo selector = goodsSearchService.getSelector(esGoodsSearchQuery);
//		return Result.success(selector);
//	}
//
//	@Operation(summary = "获取热门关键词", description = "获取热门关键词")
//	@Parameters({
//		@Parameter(name = "count", required = true, description = "热词数量"),
//	})
//	@RequestLogger
//	@PreAuthorize("hasAuthority('dept:tree:data')")
//	@GetMapping("/hot-words")
//	public Result<List<String>> getGoodsHotWords(
//		@NotNull(message = "热词数量不能为空") @RequestParam Integer count) {
//		List<String> hotWords = goodsSearchService.getHotWords(count);
//		return Result.success(hotWords);
//	}
// }
