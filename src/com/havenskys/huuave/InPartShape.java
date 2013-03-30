package com.havenskys.huuave;


public class InPartShape extends InPart {

        public World mWorld;
        public InPartShape(World world){
		super(world);
        }
        public InPartShape(World world, float left, float bottom, float back, float right, float top, float front){
		super(world);

                box(left,bottom, back, right, top, front);

        }

	public void box(float left, float bottom, float back, float right, float top, float front) {

       	GLVertex leftBottomBack = addVertex(left, bottom, back);
       GLVertex rightBottomBack = addVertex(right, bottom, back);
       	GLVertex leftTopBack = addVertex(left, top, back);
        GLVertex rightTopBack = addVertex(right, top, back);
       	GLVertex leftBottomFront = addVertex(left, bottom, front);
        GLVertex rightBottomFront = addVertex(right, bottom, front);
       	GLVertex leftTopFront = addVertex(left, top, front);
        GLVertex rightTopFront = addVertex(right, top, front);

        // vertices are added in a clockwise orientation (when viewed from the outside)
        // bottom
        addFace(new GLFace(leftBottomBack, leftBottomFront, rightBottomFront, rightBottomBack));
        // front
        addFace(new GLFace(leftBottomFront, leftTopFront, rightTopFront, rightBottomFront));
        // left
        addFace(new GLFace(leftBottomBack, leftTopBack, leftTopFront, leftBottomFront));
        // right
        addFace(new GLFace(rightBottomBack, rightBottomFront, rightTopFront, rightTopBack));
        // back
        addFace(new GLFace(leftBottomBack, rightBottomBack, rightTopBack, leftTopBack));
        // top
        addFace(new GLFace(leftTopBack, rightTopBack, rightTopFront, leftTopFront));
		
	}
	

	
}
