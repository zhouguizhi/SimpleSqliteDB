package test.com.simplesqlitedb.bean;
import com.simple.annotations.DBField;
import com.simple.annotations.DBNotField;
import com.simple.annotations.DBNotNullField;
import com.simple.annotations.DBPrivateKeyField;
import com.simple.annotations.DBTable;
import com.simple.annotations.DBNotNullAndUnique;
/**
 * Created by zhouguizhi on 2017/11/21.
 */
@DBTable(value = "tb_person")
public class Person {
    @DBPrivateKeyField("id")
    private Integer id;
    @DBField("name")
    private String name;
    @DBField("age")
    private Integer age;
    @DBField("isVip")
    private Boolean isvip;
    @DBField("salary")
    private Double salary;
    @DBNotNullAndUnique("num")
    private Integer num;
    @DBNotNullField("depmarket")
    private String depmarket;
    @DBNotField
    private String provice;
    private String company;
    public void setCompany(String company) {
        this.company = company;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setDepmarket(String depmarket) {
        this.depmarket = depmarket;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getIsvip() {
        return isvip;
    }

    public void setIsvip(Boolean isvip) {
        this.isvip = isvip;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }



    public double getSalary() {
        return salary;
    }

    public Integer getNum() {
        return num;
    }

    public String getDepmarket() {
        return depmarket;
    }

    public String getProvice() {
        return provice;
    }

    public String getCompany() {
        return company;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", isVip=" + isvip +
                ", salary=" + salary +
                ", num=" + num +
                ", depmarket='" + depmarket + '\'' +
                ", provice='" + provice + '\'' +
                ", company='" + company + '\'' +
                '}';
    }
}
