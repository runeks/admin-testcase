Test project for reproducing WELD-1802

Build and deploy, admin-frontend-testcase-4.0-SNAPSHOT.war, https://localhost:9643/ (depending on https port settings)

Use two browsers, for example chrome incognito and firefox private in order to get different sessions

With the first browser, log in any user id (user A), select role, choose "Trigger 2 minute wait for session timeout", wait a couple of minutes.

With the second, log in a different user, go to "Trigger out of memory error".  Wait a sec and see that you get WELD-000712 in the server.log.

Log in with a different user than user A.  Check the application log (notsecure.log).
The signature of the error is that uid changes from SamlAssertionFilter to SelectRoleFilter for the thread that got the WELD-000712. 
Keep refresing page until you have verified the bad thread.
