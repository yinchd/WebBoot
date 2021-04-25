package com.yinchd.web.mapper;

import com.yinchd.web.model.UserModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户管理 Mapper 接口
 * </p>
 *
 * @author yinchd
 * @since 2021-04-25
 */
@Mapper
public interface UserMapper extends BaseMapper<UserModel> {

}
