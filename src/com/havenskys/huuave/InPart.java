package com.havenskys.huuave;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import android.util.Log;

public class InPart {

	public InPart(World world) {
		mWorld = world;
	}
	
	public void addFace(GLFace face) {
		mFaceList.add(face);
	}
	
	public void setFaceColor(int face, GLColor color) {
		mFaceList.get(face).setColor(color);
	}
			
	public void putIndices(ShortBuffer buffer) {
		Iterator<GLFace> iter = mFaceList.iterator();
		while (iter.hasNext()) {
			GLFace face = iter.next();
			face.putIndices(buffer);
		}		
	}
	
	public int getIndexCount() {
		int count = 0;
		Iterator<GLFace> iter = mFaceList.iterator();
		while (iter.hasNext()) {
			GLFace face = iter.next();
			count += face.getIndexCount();
		}		
		return count;
	}
	public GLVertex addVertex(float xi, float yi, float zi) {
		
		// look for an existing GLVertex first
		Iterator<GLVertex> iter = mVertexList.iterator();
		while (iter.hasNext()) {
			GLVertex vertex = iter.next();
			if ((vertex.x == xi+x) && (vertex.y == yi+y) && (vertex.z == zi+z) ) {
				return vertex;
			}
		}

	
GLVertex vertex = mWorld.addVertex(xi+x, yi+x, zi+z);

mVertexList.add(vertex);
//Log.i(G,"shape made vertex(x"+x+",y"+y+",z"+z+") pivot x("+x+")");
		return vertex;
	}
	public void animateTransform(M4 transform) {
		mAnimateTransform = transform;
		
		if (mTransform != null)
			transform = mTransform.multiply(transform);

		Iterator<GLVertex> iter = mVertexList.iterator();
		while (iter.hasNext()) {
			GLVertex vertex = iter.next();
			mWorld.transformVertex(vertex, transform);
		}
	}
	
	public void startAnimation() {
	}

	public void endAnimation() {
		if (mTransform == null) {
			mTransform = new M4(mAnimateTransform);
		} else {
			mTransform = mTransform.multiply(mAnimateTransform);
		}
	}

    public float x = 0f;public float y = 0f;public float z = 0f;
    private static String G = "Part";

	
	public M4						mTransform;
	public M4						mAnimateTransform;
	protected ArrayList<GLFace>		mFaceList = new ArrayList<GLFace>();
	protected ArrayList<GLVertex>	mVertexList = new ArrayList<GLVertex>();
	protected ArrayList<Integer>	mIndexList = new ArrayList<Integer>();	// make more efficient?
	protected World mWorld;
}
