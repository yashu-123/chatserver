import java.io.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import java.sql.*;
public class Server 
{
	static int i=0,k;
	static String name;
	static String online="&*^%";
	static Map<String,ClientHandler> hm=new HashMap<String,ClientHandler>();
	public static void main(String arg[]) throws IOException
	{
		ServerSocket ss=new ServerSocket(2003);
		Socket s;
		Socket skt[]=new Socket[50];
		try
		{
			while(true)
			{
				skt[i]=new Socket();
				s=ss.accept();
				skt[i]=s;
				i++;
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				name=(String)ois.readObject();
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				online="&*^%";
				ClientHandler ch=new ClientHandler(s,name,ois,oos);
				hm.put(name,ch);
				for(Map.Entry<String,ClientHandler> m:Server.hm.entrySet())
				{
					online=online+"##"+m.getKey();
				}
				oos.writeObject(online);
				for(k=0;k<i;k++)
				{
					ObjectOutputStream os=new ObjectOutputStream(skt[k].getOutputStream());
					os.writeObject(online);
				}
				Thread t=new Thread(ch);
				t.start();
			}
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null,e.getMessage());
			//System.out.println(e.getMessage());
		}
	}
	
		
}
class ClientHandler implements Runnable 
{
	static int k=5;
	String name;
	ObjectInputStream ois;
	ClientHandler c;
	ObjectOutputStream oos;
	Socket s;	
	ClientHandler(Socket s,String name,ObjectInputStream ois,ObjectOutputStream oos)	
	{
		this.s=s;
		this.name=name;
		this.ois=ois;
		this.oos=oos;
	}
	public void run()
	{	
		String recieved;
		while(true)
		{
			try
			{
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				Object o=ois.readObject();
				recieved=(String)o;
				if(recieved.contains("&*$#@"))
				{	
					StringTokenizer st = new StringTokenizer(recieved, "&*$#@");
					String name = st.nextToken();
                			String MsgToSend = st.nextToken();
               	 			String recipient = st.nextToken();
					for(Map.Entry<String,ClientHandler> m:Server.hm.entrySet())
					{
						if(m.getKey().equals(recipient))
						{
							ClientHandler clh=m.getValue();
							ObjectOutputStream os=new ObjectOutputStream(clh.s.getOutputStream());
							os.writeObject(MsgToSend);
							break;
						}
					}
					Class.forName("com.mysql.jdbc.Driver");
					Connection cn=DriverManager.getConnection("jdbc:mysql://localhost/chatserver?user=root&password=india@123");
					String q="INSERT INTO total_messages (id, message_count ) VALUES (?,1)ON DUPLICATE KEY UPDATE message_count = message_count + 1;";
					PreparedStatement pst=cn.prepareStatement(q);
					String a1;
					if(name.compareTo(recipient)<0)
						a1=name+":"+recipient;
					else
						a1=recipient+":"+name;
					pst.setString(1,a1);
					pst.executeUpdate();
					q="SELECT * FROM total_messages WHERE id = '" + a1 + "';";
					PreparedStatement pst2=cn.prepareStatement(q);
					ResultSet rs=pst2.executeQuery();
					rs.next();
					int x=rs.getInt(2);
					q="INSERT INTO messages (id_message_number, message, sender)VALUES (?,?,?);";
					PreparedStatement pst3=cn.prepareStatement(q);
					pst3=cn.prepareStatement(q);
					pst3.setString(1,a1+":"+x);
					pst3.setString(2,MsgToSend);
					pst3.setString(3,name);
					pst3.executeUpdate();
					cn.close();					
				}
				else if(recieved.contains("123*&^%"))
				{
					String temp=recieved.substring(7,recieved.length());
					Server.hm.remove(temp);
					String onl="1876!@*$!&";
					for(Map.Entry<String,ClientHandler> m:Server.hm.entrySet())
					{
						onl=onl+"##"+m.getKey();
					}
					for(Map.Entry<String,ClientHandler> m:Server.hm.entrySet())
					{
						ClientHandler cl=m.getValue();
						ObjectOutputStream oos=new ObjectOutputStream(cl.s.getOutputStream());
						oos.writeObject(onl);						
					}
					this.s.close();	
					return;	
				}
				else if(recieved.contains("prechat@#$%"))
				{
					recieved=recieved.substring(11,recieved.length());
					StringTokenizer st = new StringTokenizer(recieved, "##");
               	 			String recipient = st.nextToken();
					String name = st.nextToken();
					String a1;
					if(name.compareTo(recipient)<0)
						a1=name+":"+recipient;
					else
						a1=recipient+":"+name;
					Class.forName("com.mysql.jdbc.Driver");
					Connection cn=DriverManager.getConnection("jdbc:mysql://localhost/chatserver?user=root&password=india@123");
					String q="SELECT * FROM total_messages WHERE id = '" + a1 + "';";
					PreparedStatement pst=cn.prepareStatement(q);
					ResultSet rs=pst.executeQuery();
					if(rs.next())
					{
						int x=rs.getInt(2);
						for(Map.Entry<String,ClientHandler> m:Server.hm.entrySet())
						{
							if(m.getKey().equals(name))
							{
								c=m.getValue();
								break;
							}
						}
						ObjectOutputStream os=new ObjectOutputStream(c.s.getOutputStream());
						for(int i=0;i<=x;i++)
						{
							if(i==0)
							{
								os.writeObject("%^)(&*"+x);
							}
							else
							{
								String temp=a1+":"+i;
								q="SELECT * FROM messages WHERE id_message_number = '" + temp + "';";
								PreparedStatement pst2=cn.prepareStatement(q);
								ResultSet rst=pst2.executeQuery();
								rst.next();
								String msg=rst.getString(2);
								String sender =rst.getString(3);
								os.writeObject(sender+":-"+msg);	
							}		
						}
						cn.close();
					}
					else
					{
						for(Map.Entry<String,ClientHandler> m:Server.hm.entrySet())
						{
							if(m.getKey().equals(name))
							{
								c=m.getValue();
								break;
							}
						}
						new ObjectOutputStream(c.s.getOutputStream()).writeObject("%^)(&*0");
					}	
				}
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(null,e.getMessage());
				//System.out.println(e.getMessage()+"Oye");
			}
		}
	}
	
}
