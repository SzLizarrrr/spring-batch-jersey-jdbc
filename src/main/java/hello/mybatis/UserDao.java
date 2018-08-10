package hello.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
	
	private final SqlSession sqlSession;
	
	public UserDao(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public User selectUserByName(String name) {
		return this.sqlSession.selectOne("findByName", name);
	}

}
