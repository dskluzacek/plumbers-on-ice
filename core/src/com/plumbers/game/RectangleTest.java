package com.plumbers.game;

public class RectangleTest {

	public static void main(String[] args) {
		Rectangle rect = new Rectangle(100, 100, 20, 30);
		
		Rectangle rect2 = new Rectangle(120, 130, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.TOP );
		assert( rect.collisionInfo(rect2).getDistance() == 0 );
		
		rect2 = new Rectangle(90, 130, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.TOP );
		assert( rect.collisionInfo(rect2).getDistance() == 0 );
		
		rect2 = new Rectangle(120, 90, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.LEFT );
		assert( rect.collisionInfo(rect2).getDistance() == 0 );
		
		rect2 = new Rectangle(90, 90, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.BOTTOM );
		assert( rect.collisionInfo(rect2).getDistance() == 0 );
		
		rect2 = new Rectangle(90, 100, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.RIGHT );
		assert( rect.collisionInfo(rect2).getDistance() == 0 );

		rect2 = new Rectangle(105, 129, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.TOP );
		assert( rect.collisionInfo(rect2).getDistance() == 1 );
		
		rect2 = new Rectangle(115, 125, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.TOP );
		assert( rect.collisionInfo(rect2).getDistance() == 5 );
		
		rect2 = new Rectangle(115, 124, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.LEFT );
		assert( rect.collisionInfo(rect2).getDistance() == 5 );
		
		rect2 = new Rectangle(115, 94, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.BOTTOM );
		assert( rect.collisionInfo(rect2).getDistance() == 4 );

		rect2 = new Rectangle(115, 96, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.LEFT );
		assert( rect.collisionInfo(rect2).getDistance() == 5 );
		
		rect2 = new Rectangle(95, 97, 10, 10);
		print( rect.collisionInfo(rect2) );
		assert( rect.collisionInfo(rect2).getDirection() == Direction.RIGHT );
		assert( rect.collisionInfo(rect2).getDistance() == 5 );
	}
	
	static void print(Rectangle.Collision info) {
		if (info != null)
		{
        	System.out.print(info.getDirection() + ", ");
        	System.out.println(info.getDistance());
		}
		else
			System.out.println("(null)");
	}
}
