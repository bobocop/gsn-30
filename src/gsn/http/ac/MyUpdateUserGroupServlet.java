package gsn.http.ac;

import gsn.Main;
import gsn.beans.ContainerConfig;
import gsn.http.WebConstants;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Behnaz Bostanipour
 * Date: Apr 26, 2010
 * Time: 6:24:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyUpdateUserGroupServlet extends HttpServlet
{
    /****************************************** Servlet Methods*******************************************/
    /****************************************************************************************************/
    
    public void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException
    {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        // Get the session
        HttpSession session = req.getSession();
        ConnectToDB ctdb = null;

        User user = (User) session.getAttribute("user");
        if (user == null)
       {
        	UserUtils.redirectToLogin(req,res);
       }
       else
       {
           this.checkSessionScheme(req,res);
           if(!user.getUserName().equals("Admin"))
           {
               res.sendError( WebConstants.ACCESS_DENIED , "Access denied." );
           }
           else
           {
               ParameterSet pm = new ParameterSet(req);
               if(pm.valueForName("groupname")==null|| pm.valueForName("grouptype")==null|| pm.valueForName("update")==null || pm.valueForName("username")==null )
               {
                   res.sendRedirect("/");
                   return;
               }
               if(pm.valueForName("groupname").equals("")|| pm.valueForName("grouptype").equals("")|| pm.valueForName("update").equals("")|| pm.valueForName("username").equals("") )
               {
                   res.sendRedirect("/");
                   return;
               }
               try
               {
                   ctdb = new ConnectToDB();
                   User waitingUser = new User(pm.valueForName("username"));

                   if(pm.valueForName("update").equals("yes"))
                   {
                       if(pm.valueForName("grouptype").equals("5"))
                       {
                           waitingUser.setIsWaiting("no");
                           ctdb.updateGroupForUser(waitingUser,new Group(pm.valueForName("groupname"),"n"));
                       }
                       else if(pm.valueForName("grouptype").equals("0"))
                       {
                           ctdb.deleteGroupForUser(new Group(pm.valueForName("groupname")), waitingUser);
                       }
                   }
                   else if(pm.valueForName("update").equals("no"))
                   {
                       if(pm.valueForName("grouptype").equals("5"))
                       {
                           ctdb.deleteGroupForUser(new Group(pm.valueForName("groupname")), waitingUser);
                       }
                       else if(pm.valueForName("grouptype").equals("0"))
                       {
                           waitingUser.setIsWaiting("no");
                           ctdb.updateGroupForUser(waitingUser,new Group(pm.valueForName("groupname"),"n"));

                       }

                   }

                    res.sendRedirect("/gsn/MyUserUpdateWaitingListServlet");
               }
               catch(Exception e)
               {
                   out.println(" MyUpdateUserGroupServlet Exception caught : "+e.getMessage());
               }
               finally
               {
                   if(ctdb!=null)
                   {
                       ctdb.closeStatement();
                       ctdb.closeConnection();
                   }
               }
           }
       }
    }


    public void doGet(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException
    {
           this.doPost(req,res);
    }
    /****************************************** Client Session related Methods*******************************************/
    /********************************************************************************************************************/


    private void checkSessionScheme(HttpServletRequest req, HttpServletResponse res)throws IOException
    {

         if(req.getScheme().equals("https")== true)
        {
            if((req.getSession().getAttribute("scheme")==null))
            {
                req.getSession().setAttribute("scheme","https");
            }
        }
         else if(req.getScheme().equals("http")== true )
        {
             if((req.getSession().getAttribute("scheme")==null))
            {
                req.getSession().setAttribute("scheme","http");
            }
            res.sendRedirect("https://"+req.getServerName()+":"+ Main.getContainerConfig().getSSLPort()+"/gsn/MyUpdateUserGroupServlet");

        }
    }



}
