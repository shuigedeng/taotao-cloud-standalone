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

package com.taotao.cloud.standalone.infrastructure.persistent.dict.dataobject;

import lombok.*;

@Setter
@Getter
@ToString
public class DictDeptDO {

    // dict
    private Long id;
    private Long dictId;
    private String itemText;
    private String itemValue;
    private String description;
    private Integer status;

    // dept
    private Integer deptId;
    private String name;
    private Integer parentId;
    private Integer sort;
}
