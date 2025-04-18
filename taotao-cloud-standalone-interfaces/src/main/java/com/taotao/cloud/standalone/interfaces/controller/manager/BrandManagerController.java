package com.taotao.cloud.standalone.interfaces.controller.manager; /// *
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
// package com.taotao.cloud.standalone.facade.controller.manager;
//
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.taotao.boot.common.model.PageResult;
// import com.taotao.boot.common.model.Result;
// import com.taotao.cloud.standalone.application.command.brand.dto.clientobject.BrandCO;
// import com.taotao.cloud.standalone.application.convert.BrandConvert;
// import com.taotao.cloud.standalone.application.service.IBrandService;
// import com.taotao.cloud.standalone.infrastructure.persistent.po.BrandPO;
// import com.taotao.boot.web.request.annotation.RequestLogger;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.Parameters;
// import io.swagger.v3.oas.annotations.enums.ParameterIn;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import jakarta.validation.constraints.NotBlank;
// import java.util.List;
// import lombok.AllArgsConstructor;
// import nl.basjes.parse.useragent.clienthints.ClientHints.Brand;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
/// **
// * 管理端-品牌管理API
// *
// * @author shuigedeng
// * @version 2022.04
// * @since 2022-04-25 16:16:20
// */
// @AllArgsConstructor
// @Validated
// @RestController
// @Tag(name = "管理端-品牌管理API", description = "管理端-品牌管理API")
// @RequestMapping("/goods/manager/brand")
// public class BrandManagerController extends BusinessController {
//
//    /** 品牌 */
//    private final IBrandService brandService;
//
//    @Operation(summary = "通过id获取", description = "通过id获取")
//    @Parameters({
//            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
//    })
//    @RequestLogger
//    @PreAuthorize("hasAuthority('dept:tree:data')")
//    @GetMapping(value = "/{id}")
//    public Result<BrandCO> getById(@NotBlank(message = "id不能为空") @PathVariable Long id) {
//        BrandPO brand = brandService.getById(id);
//        return Result.success(BrandConvert.INSTANCE.convert(brand));
//    }
//
//    @Operation(summary = "获取所有可用品牌", description = "获取所有可用品牌")
//    @Parameters({
//            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
//    })
//    @RequestLogger
//    @PreAuthorize("hasAuthority('dept:tree:data')")
//    @GetMapping(value = "/all/available")
//    public Result<List<BrandCO>> getAllAvailable() {
//        List<BrandPO> list = brandService.getAllAvailable();
//        return Result.success(BrandConvert.INSTANCE.convert(list));
//    }
//
//    @Operation(summary = "分页获取", description = "分页获取")
//    @Parameters({
//            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
//    })
//    @RequestLogger
//    @PreAuthorize("hasAuthority('dept:tree:data')")
//    @GetMapping(value = "/page")
//    public Result<PageResult<BrandCO>> brandsQueryPage(@Validated BrandPageQuery page) {
//        IPage<BrandPO> brandPage = brandService.brandsQueryPage(page);
//        return Result.success(MpUtils.convertMybatisPage(brandPage, BrandCO.class));
//    }
//
//    @Operation(summary = "新增品牌", description = "新增品牌")
//    @Parameters({
//            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
//    })
//    @RequestLogger
//    @PreAuthorize("hasAuthority('dept:tree:data')")
//    @PostMapping
//    public Result<Boolean> save(@Validated @RequestBody BrandDTO brand) {
//        return Result.success(brandService.addBrand(brand));
//    }
//
//    @Operation(summary = "更新品牌", description = "更新品牌")
//    @Parameters({
//            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
//    })
//    @RequestLogger
//    @PreAuthorize("hasAuthority('dept:tree:data')")
//    @PutMapping("/{id}")
//    public Result<Boolean> update(@PathVariable Long id, @Validated BrandDTO brand) {
//        brand.setId(id);
//        return Result.success(brandService.updateBrand(brand));
//    }
//
//    @Operation(summary = "后台禁用品牌", description = "后台禁用品牌")
//    @Parameters({
//            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
//    })
//    @RequestLogger
//    @PreAuthorize("hasAuthority('dept:tree:data')")
//    @PutMapping(value = "/disable/{brandId}")
//    public Result<Boolean> disable(@PathVariable Long brandId, @RequestParam Boolean disable) {
//        return Result.success(brandService.brandDisable(brandId, disable));
//    }
//
//    @Operation(summary = "批量删除", description = "批量删除")
//    @Parameters({
//            @Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
//    })
//    @RequestLogger
//    @PreAuthorize("hasAuthority('dept:tree:data')")
//    @DeleteMapping(value = "/{ids}")
//    public Result<Boolean> delAllByIds(@PathVariable List<Long> ids) {
//        return Result.success(brandService.deleteBrands(ids));
//    }
// }
