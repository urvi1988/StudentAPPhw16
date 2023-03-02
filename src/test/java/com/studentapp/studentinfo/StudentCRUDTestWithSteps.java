package com.studentapp.studentinfo;

import com.studentapp.testbase.TestBase;
import com.studentapp.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class StudentCRUDTestWithSteps extends TestBase {
    static String firstName = "urvi"+ TestUtils.getRandomValue();
    static String lastName = "patel"+ TestUtils.getRandomValue();
    static String email = "uv"+ TestUtils.getRandomValue()+ "@gmail.com";
    static String programme = "sft testing";
    static int studentId;

    @Steps
    StudentSteps studentSteps;

    @Title("This will create a new student")
    @Test
    public void test001() {
        List<String> courses= new ArrayList<>();
        courses.add("BBA");
        courses.add("MBA");

        ValidatableResponse response =studentSteps.createStudent(firstName,lastName,email,programme,courses);
        response.statusCode(201);

    }

    @Title("verify if student is created")
    @Test
    public void test002() {
        HashMap<String,Object> studentMapData =studentSteps.getStudentInfoByFirstName(firstName);
        Assert.assertThat(studentMapData,hasValue(firstName));
        studentId= (int) studentMapData.get("id");
        System.out.println(studentId);

    }

    @Title("update the user information")
    @Test
    public void test003() {
        firstName = firstName + "Queen";

        List<String> courses = new ArrayList<>();
        courses.add("BBA");
        courses.add("MBA");
        studentSteps.updateStudent(studentId,firstName,lastName,email,programme,courses);
        HashMap<String,Object> studentMap=studentSteps.getStudentInfoByFirstName(firstName);
        Assert.assertThat(studentMap,hasValue(firstName));

    }
    @Title("Delete student info by StudentID and verify its deleted")
    @Test
    public void test004(){

        studentSteps.deleteStudentInfoByID(studentId).statusCode(204);
        studentSteps.getStudentInfoByStudentId(studentId).statusCode(404);

    }

}
