package com.yinchd.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yinchd.web.model.UserModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户管理 Mapper 接口
 * </p>
 *
 * @author yinchd
 * @since 2021-03-29
 */
@Mapper
public interface UserMapper extends BaseMapper<UserModel> {

}
