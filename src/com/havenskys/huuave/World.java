/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.havenskys.huuave;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Iterator;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import javax.microedition.khronos.opengles.GL10;

public class World {
private static String G = "glworld";
	public void addShape(InPart shape) {
		mShapeList.add(shape);
		mIndexCount += shape.getIndexCount();
	}
	
	public void generate() {		
	    ByteBuffer bb = ByteBuffer.allocateDirect(mVertexList.size()*4*4);
	    bb.order(ByteOrder.nativeOrder());
		mColorBuffer = bb.asIntBuffer();

	    bb = ByteBuffer.allocateDirect(mVertexList.size()*4*3);
	    bb.order(ByteOrder.nativeOrder());
	    mVertexBuffer = bb.asIntBuffer();
           
            bb = ByteBuffer.allocateDirect(mVertexList.size()*3*4);
            bb.order(ByteOrder.nativeOrder());
                        mTextureBuffer = bb.asIntBuffer();

	    bb = ByteBuffer.allocateDirect(mIndexCount*2);
	    bb.order(ByteOrder.nativeOrder());
	    mIndexBuffer = bb.asShortBuffer();


		Iterator<GLVertex> iter2 = mVertexList.iterator();
		while (iter2.hasNext()) {
			GLVertex vertex = iter2.next();
			vertex.put(mVertexBuffer, mColorBuffer, mTextureBuffer);
		}

		Iterator<InPart> iter3 = mShapeList.iterator();
		while (iter3.hasNext()) {
			InPart shape = iter3.next();
			shape.putIndices(mIndexBuffer);
		}
	}
        public float mPivotx = 0f;
        public float mPivoty = 0f;
        public float mPivotz = 0f;
        public float mTrimy = 0f;
        public float mTrimx = 0f;
        public float mTrimz = 0f;	
	public GLVertex addVertex(float x, float y, float z) {


		GLVertex vertex = new GLVertex(x+mTrimx, y+mTrimy, z+mTrimz, mVertexList.size());
		mVertexList.add(vertex);
//Log.i(G,"glworld made vertex(x"+x+",y"+y+",z"+z+") pivot(x"+mPivotx+",y"+mPivoty+",z"+mPivotz+") ("+mVertexList.size()+")");
		return vertex;
	}
	
	public void transformVertex(GLVertex vertex, M4 transform) {
	
		
		
		
        if(mPivotx > 0.0f || mPivoty > 0.0f || mPivotz > 0.0f){
    		//Iterator<GLVertex> iter2 = mVertexList.iterator();
    		//while (iter2.hasNext()) {
    			//GLVertex vertex = iter2.next();

                            
//                            while(vertex.x > 360){vertex.x-=360;}while(vertex.x < 360){vertex.x+=360;}                
//                            while(vertex.y > 360){vertex.y-=360;}while(vertex.y < 360){vertex.y+=360;}
//                            while(vertex.z > 360){vertex.z-=360;}while(vertex.z < 360){vertex.z+=360;}

//    			Log.w(G,"vertex(x"+vertex.x+",y"+vertex.y+",z"+vertex.z+") trim(x"+mPivotx+",y"+mPivoty+",z"+mPivotz+")");

//    			vertex.x += mPivotx;
//    			vertex.y += mPivoty;
//    			vertex.z += mPivotz;


                  //  } 
    		//mVertexBuffer.position(0);

        	transform.setPivot(mPivotx, mPivoty, mPivotz);
        	
        }
		
		
		
		
		
		vertex.update(mVertexBuffer, transform);

	
	
	
	
	
	
	}

public float getZ(int vertex){        
int i = 0;
Iterator<GLVertex> iter2 = mVertexList.iterator();
while(iter2.hasNext()){
i++;
GLVertex v = iter2.next();
if(i == vertex){return v.z;}

}
Log.e(G,"requested vertex didn't exist "+vertex + " of " + i);
return 1;
}

public float getX(int vertex){        
int i = 0;
Iterator<GLVertex> iter2 = mVertexList.iterator();
while(iter2.hasNext()){
i++;
GLVertex v = iter2.next();
if(i == vertex){return v.x;}

}
Log.e(G,"requested vertex didn't exist "+vertex + " of " + i);
return 1;
}

public float getY(int vertex){        
int i = 0;
Iterator<GLVertex> iter2 = mVertexList.iterator();
while(iter2.hasNext()){
i++;
GLVertex v = iter2.next();
if(i == vertex){return v.y;}

}
Log.e(G,"requested vertex didn't exist "+vertex + " of " + i);
return 1;
}
	int count = 0;
    public void draw(GL10 gl)
    {
		mColorBuffer.position(0);
		mVertexBuffer.position(0);
		mIndexBuffer.position(0);
        mTextureBuffer.position(0);  


		gl.glFrontFace(GL10.GL_CCW);
//        gl.glShadeModel(GL10.GL_FLAT);
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
gl.glEnable(GL10.GL_TEXTURE_2D);
//        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, mIndexCount, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
        count++;

    }
   
    static public float toFloat(int x) {
    	return x/65536.0f;
    }

	private ArrayList<InPart>	mShapeList = new ArrayList<InPart>();	
	private ArrayList<GLVertex>	mVertexList = new ArrayList<GLVertex>();
	
	private int mIndexCount = 0;

    private IntBuffer   mVertexBuffer;
    private IntBuffer   mColorBuffer;
    private ShortBuffer mIndexBuffer;
    private IntBuffer   mTextureBuffer;




}
