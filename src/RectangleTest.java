
public class RectangleTest
{

	public static void main(String[] args)
	{
		Rectangle rect = new Rectangle(0, 0, 10, 10);
		Rectangle rect2 = new Rectangle(10, 10, 10, 10);
		
		Rectangle.CollisionInfo info = rect.collisionInfo(rect2);
		
		if (info != null)
		{
        	System.out.println(info.getDirection());
        	System.out.println(info.getDistance());
		}
		else
			System.out.println("null");
	}

}
