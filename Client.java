import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
public class Client implements ActionListener
{
	Frame f;
	static TextField t;
	Button b;
	Label la,la2;
	List l;
	Client()
	{
		f=new Frame("Messenger");
		f.setBackground(new Color(238,241,246));
		b=new Button("Submit");
		t=new TextField();
		la=new Label("Enter Name");
		la.setForeground(new Color(0,28,76));
		la2=new Label(" Login");
		la2.setFont(new Font("Arial",1,15));
		la.setFont(new Font("Arial",1,15));
		la2.setBackground(new Color(59,89,152));
		la2.setForeground(Color.white);
		f.setLayout(null);
		f.setSize(250,250);
		la.setBounds(10,80,240,20);
		la2.setBounds(10,30,240,30);
		t.setBounds(10,110,180,30);
		b.setBounds(70,150,60,30);
		b.setBackground(new Color(59,89,152));
		b.setForeground(Color.white);
		f.add(la);
		f.add(la2);
		f.add(t);
		f.add(b);
		b.addActionListener(this);
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}
	public static void main(String arg[])
	{
		Client c=new Client();
	}
	public void actionPerformed(ActionEvent e)
	{
		f.setVisible(false);
		Thread t=new Thread(new Help());
		t.start();
		MyList ml=new MyList();
	}
}
class MyFrame implements ActionListener
{
	static String name;
	static Frame f;
	Button b,b1;
	TextField t;
	Panel p;
	static Label la;
	static List l;
	static Socket s;
	MyFrame()
	{
		f=new Frame("Messenger");
		b=new Button("Send");
		b1=new Button("Logout");
		b.setBackground(new Color(59,89,152));
		b.setForeground(Color.white);
		b1.setBackground(new Color(59,89,152));
		b1.setForeground(Color.white);
		t=new TextField(30);
		p=new Panel();
		la=new Label();
		la.setBackground(new Color(59,89,152));
		la.setForeground(Color.white);
		l=new List();
		p.add(t);
		p.add(b);
		p.add(b1);
		f.setSize(400,400);
		f.add(p,BorderLayout.SOUTH);
		f.add(la,BorderLayout.NORTH);
		f.add(l);
		b.addActionListener(this);
		b1.addActionListener(this);
		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				JOptionPane.showMessageDialog(null,"Logout First");
			}
		});
		try
		{
			s=new Socket("localhost",2003);
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(Client.t.getText());
			name=Client.t.getText();
			while(true)
			{	
				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
				String msg=(String)ois.readObject();
				if(msg.contains("&*^%"))
				{
					MyList.l.removeAll();
					String temp[]=msg.split("##");
					for(String x:temp)
					{
						if(!(x.equals("&*^%"))&&!(x.equals(Client.t.getText())))
							MyList.l.add(x);
					}	
				}
				else if(msg.contains("1876!@*$!&"))
				{
					MyList.l.removeAll();
					String temp[]=msg.split("##");
					for(String x:temp)
					{
						if(!(x.equals("1876!@*$!&"))&&!(x.equals(Client.t.getText())))
							MyList.l.add(x);
					}		
				}
				else if(msg.contains("%^)(&*"))
				{
					msg=msg.substring(6,msg.length());
					int x=Integer.parseInt(msg);
					for(int i=1;i<=x;i++)
					{
						msg=(String)ois.readObject();
						java.util.StringTokenizer st=new java.util.StringTokenizer(msg,":-");
						String temp=st.nextToken();
						msg=st.nextToken();
						if(temp.equals(name))
							msg="Me:-"+msg;
						else
							msg=temp+":-"+msg;
						l.add(msg);
					}

				}
				else
				{
					l.add(la.getText()+":-"+msg);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		try
		{
			if(e.getSource()==b)
			{
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(name+"&*$#@"+t.getText()+"&*$#@"+la.getText());
				l.add("Me:-"+t.getText());
				t.setText("");
			}
			if(e.getSource()==b1)
			{
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				oos.writeObject("123*&^%"+name);
				//s.close();
				f.dispose();	
				MyList.f.dispose();
				System.exit(0);	
			}
		}
		catch(Exception e1)
		{
			System.out.println(e1.getMessage());
		}
	}
}
class MyList implements ActionListener
{
	int k=0;
	static Frame f;
	static List l;
	Button b;
	MyList()
	{
		f=new Frame("Online Friends");
		l=new List();
		//l.add("Ram");
		b=new Button("Select");
		b.setBackground(new Color(59,89,152));
		b.setForeground(Color.white);
		f.add(l);
		f.add(b,BorderLayout.NORTH);
		f.setSize(225,300);
		f.setVisible(true);
		b.addActionListener(this);
		f.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				int a=JOptionPane.showConfirmDialog(null,"Do you really want to exit?");
				try
				{
					if(a==0)
					{
						ObjectOutputStream oos=new ObjectOutputStream(MyFrame.s.getOutputStream());
						oos.writeObject("123*&^%"+MyFrame.name);
						//s.close();
						f.dispose();	
						MyList.f.dispose();
						System.exit(0);
					}	
				}
				catch(Exception e1)
				{
					JOptionPane.showMessageDialog(null,e1.getMessage());
				}
			}
		});
	}
	public void actionPerformed(ActionEvent e)
	{
		if(l.getItemCount()==0)
		{
			JOptionPane.showMessageDialog(null,"List is Empty");
		}
		else if(l.getSelectedIndex()==-1)
		{
			JOptionPane.showMessageDialog(null,"No Friend is selected");			
		}
		else
		{
			try
			{
				MyFrame.l.removeAll();
				MyFrame.la.setText(l.getItem(l.getSelectedIndex()));
				ObjectOutputStream oos=new ObjectOutputStream(MyFrame.s.getOutputStream());
				String a1="prechat@#$%";
				oos.writeObject(a1+"##"+l.getItem(l.getSelectedIndex())+"##"+MyFrame.name);
				MyFrame.f.setVisible(true);
			}
			catch(Exception e1)
			{
			}
		}
		
	}		
}
class Help implements Runnable
{
	public void run()
	{
		MyFrame mf=new MyFrame();
	}
}