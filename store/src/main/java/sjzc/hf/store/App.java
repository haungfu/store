package sjzc.hf.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages= {"sjzc.hf.miaosha"})
@MapperScan("sjzc.hf.miaosha.dao")
public class App {
	
	
    public static void main( String[] args ){
        SpringApplication.run(App.class, args);
    }
  
}
