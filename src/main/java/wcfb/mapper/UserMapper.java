package wcfb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wcfb.model.po.UserPo;

/**
 * @Author wcfb
 * @Time 2020/1/5
 */
@Mapper
public interface UserMapper extends BaseMapper<UserPo> {

    int insert(UserPo userPo);

    void update(UserPo userPo);

    UserPo select(Integer id);

    String loginByAccount(@Param("account") String account, @Param("password") String password);

    String loginByPhone(@Param("phone") String phone, @Param("password") String password);

    String loginByEmail(@Param("email") String email, @Param("password") String password);

    UserPo selectByPhone(String phone);

    int updatePassword(UserPo userPo);

    UserPo selectByEmail(String email);
}
