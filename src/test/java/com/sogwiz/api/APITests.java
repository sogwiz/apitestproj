package com.sogwiz.api;

import com.google.gson.internal.LinkedTreeMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by sogwiz on 2/2/17.
 */
public class APITests extends BaseTest {
    @Test(dataProvider = "posts")
    public void testBlogPosts(Integer postNum, boolean flag) throws Exception{
        SogwizAPIService service = new SogwizAPIService();
        LinkedTreeMap obj = service.getClient().getPost(postNum).execute().body();
        System.out.println(obj);
    }

    @DataProvider(name="posts")
    public static Object[][] postNumbers(){
        return new Object[][]{{1, true}, {2, true}, {3, true},
                {4, true}};
    }
}
