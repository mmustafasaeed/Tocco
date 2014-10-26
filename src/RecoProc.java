
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JOptionPane;


public class RecoProc {
	
	final int INTERVALS = 40; // Factor of resamping interspacing distance
	final double TAIL = 0.5;  // Threshold of curvature for removing tail
	final double PERIM = 70;  // Minimum perimeter for removing tail
	final double NUMP = 5;  // Minimum number of points for removing tail
	final double OPEN = 0.16;  // Minmum ratio of distance of endpoints and perimeter for open shape
	final double T1 = 0.99;  // Maximum ratio of Euclidean distance and path length for adding corners
	final double T2 = 0.95;  // Minimum ratio of Euclidean distance and path length for removing corners
	final double DIST = 0.14;  // Ratio of perpendicular distance and chord length
	final double DIS = 11;  // Threshold of the perpendicular distance from a point to a line
	
	// Fields
	Arrows a;
	Ellipses e;
	Circles c;
	CubicCurves cc;
	Datastores ds;
	Lines l;
	Polygons pg;
	Polylines pl;
	QuadCurves qc;
	Rectangles r;
	Parallelograms po;
	Squares s;
	double perimeter;
	

	
	
	
	
	// Constructor
	public RecoProc(Polygon s, Scene s1) {

		recognise(s, s1);
		
	}
	
	// Recognise shape type
	private void recognise(Polygon s, Scene sc) {
		int i;
		
		
		
		ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		
		for (i = 0; i < s.npoints; i++)  // Store points of original shape in an arraylist
			points.add(new Point2D.Float(s.xpoints[i], s.ypoints[i]));
		
		points = reSample(s, points);  // Resample points
		
		perimeter = calPerimeter(points);  // Perimeter of shape after resampling
		
		if (perimeter > PERIM && points.size() > NUMP)  // Remove tail
			points = removeTail(points);
		

		
		perimeter = calPerimeter(points);  // New perimeter after removing tail
		System.out.println("perimeter "+perimeter+ "  npoints "+points.size());
		
		indices = findCorners(points);  // Find indices of corners
		
		boolean curved = isCurved(points, indices);  // If any curve between two corners
		System.out.println("corners: "+indices.size());
		boolean open = isopen(points, indices);  // Close or Open shape
		System.out.println("open? " + open + " curved?" + curved);
		
		// Line
		if (indices.size() == 2 && open) {
			sc.addShape(constructLine(points));
			return;
		}
		
		ArrayList<Point2D.Float> corners = new ArrayList<Point2D.Float>();  // Points of corners
		for (i = 0; i < indices.size(); i++) {
			corners.add(points.get(indices.get(i)));
			
			System.out.println("corner : "+ corners.get(i).x+" : "+ corners.get(i).y);
		}
		
		double[] corners_a = new double[indices.size()-1];  // Turned Angles of corners
		turnedAngles(corners, corners_a);
		
		double sum = 0;  // Sum of total turned angles and the average turned angle
		for (i = 1; i < corners_a.length; i++)
			sum += corners_a[i];
		sum = Math.abs(sum);
		double avg = sum / (corners_a.length-1);
		System.out.println("sum and avg of turned angles :"+sum+" "+avg);
		
		// Special shape - Arrow
		if (corners.size() > 3 && corners.size() < 7 && open) {  // The number of corners must be 4, 5 or 6
			sc.setGravity(isArrow(corners, corners_a));
				return;
		}
		
		// DataStore
		if (corners.size() == 4) {
			if (sum > 2.8 && sum < 3.5)
				if (avg > 1.3 && avg < 1.8)
					if (isDatastores(corners, corners_a))
						return;
		}
		
		// Rectangle or Square
		if (corners.size() > 4 && corners.size() < 7 && !open && !curved) {  // close shape and the number of corners is 5 or 6
			if (sum > 4 && sum < 7) {  // 3 right-angles
				if (avg > 1.3 && avg < 2) {  // the average = a right-angle?
					sc.addShape(constructRectangle(s, corners, corners_a));
						return;
				}
			}
		}
		
		// Parallelogram
		if (corners.size() == 5 && !open) {  // close shape and the number of corners is 5
			if (sum > 4 && sum < 7) {
				if (avg > 1.3 && avg < 2) {
					if (isParallelogram(corners, corners_a))
						return;
				}
			}
		}
		// check polygon		
		// Polygon
		if (!open && !curved) {
			sc.addShape(constructPolygon(corners));
			return;
		}
		
		// Ellipse or Circle
		if (corners.size() > 4 && !open) {
			if (sum > 4 && sum < 8.5) {
				if (avg > 0.4 && avg < 2) {
					sc.addShape(constructEllipse(s, corners, corners_a));
					return;
				}
			}
		}
		
		// CubicCurve
		if (corners.size() > 3 && corners.size() < 12 && open && curved) {
			if (sum < 1.5 && avg < 0.6) {
				if(isCubicCurve(corners, corners_a))
					return;
			}
		}
		
		// QuadCurve
		if (corners.size() > 2 && corners.size() < 12 && open && curved) {
			if (sum < 6  && avg < 1.5) {
				isQuadCurve(s, corners, corners_a);
				return;
			}
		}

		int[] xpoints = new int[indices.size()];
		int[] ypoints = new int[indices.size()];
		for (i = 0; i < indices.size(); i++) {
			xpoints[i] = Math.round(points.get(indices.get(i)).x);
			ypoints[i] = Math.round(points.get(indices.get(i)).y);
		}
		pl = new Polylines(xpoints, ypoints, xpoints.length);
		
		if (corners.size() > 5) {
			if (sum > 8.5) {
				if (avg > 0.6 && avg < 2) {
					System.out.println("spiral");
				}
			}
		}
	}

	public ArrayList<Point2D.Float> reSample(Polygon s, ArrayList<Point2D.Float> points) {
		
		double x1, y1, x2, y2, x, y, d, D = 0, delta;
		ArrayList<Point2D.Float> newp = new ArrayList<Point2D.Float>();
		int i;
		
		// Determine resampling spacing
		x1 = s.getBounds().getMinX();
		y1 = s.getBounds().getMinY();
		x2 = s.getBounds().getMaxX();
		y2 = s.getBounds().getMaxY();
		double diagonal = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
		
		// Intervals (default: 40) is determined empirically
		double S = diagonal / INTERVALS;

		// Start point
		newp.add(points.get(0));
		
		// Resample points
		for (i = 1; i < points.size(); i++) {
			x1 = points.get(i-1).x;
			y1 = points.get(i-1).y;
			x2 = points.get(i).x;
			y2 = points.get(i).y;
			
			// Distance between two adjacent points
			d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
			
			if ((D + d) >= S) {
				delta = (S - D)/d;
				x = x1 + delta * (x2 - x1);
				y = y1 + delta * (y2 - y1);
				Point2D.Float p = new Point2D.Float((float)x, (float)y);
				newp.add(p);
				points.add(i, p);
				D = 0;
			}
			else
				D = D + d;
		}
		
		return newp;
	}
	
	public double calPerimeter(ArrayList<Point2D.Float> points) {
		
		float x1, y1, x2, y2;
		double d, p = 0;
		
		for (int i = 1; i < points.size(); i++) {
			x1 = points.get(i-1).x;
			y1 = points.get(i-1).y;
			x2 = points.get(i).x;
			y2 = points.get(i).y;
			
			d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
			
			p += d;
		}
		
		return p;
	}

	public ArrayList<Point2D.Float> removeTail(ArrayList<Point2D.Float> points) {
		
		float x1, y1, x2, y2;
		double d, curvature, max = Double.MIN_VALUE;
		int i, sMax = 0, eMax = 0, s, e;
		
		double[] t_angles = new double[points.size()-1];
		turnedAngles(points, t_angles);  // Turned Angles
		
		s = points.size() * 15 / 100;  // The first 15% of the points
		e = points.size() * 95 / 100;  // The last 5% of the points (due to recognise arrows, have to minimise)
		
		for (i = 1; i <= s; i++) {  // The highest curvature in the first 15% of points
			x1 = points.get(i-1).x;
			y1 = points.get(i-1).y;
			x2 = points.get(i).x;
			y2 = points.get(i).y;
			
			d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
			curvature = Math.abs(t_angles[i]/d);
			
			if (max < curvature) {
				max = curvature;
				sMax = i;
			}
		}		
		if (max < TAIL)  // check whether max is greater than threshold TAIL or not
			sMax = 0;
		
		max = Double.MIN_VALUE;	
		for (i = e; i < points.size()-1; i++) {  // The highest curvature in the last 5% of points
			x1 = points.get(i-1).x;
			y1 = points.get(i-1).y;
			x2 = points.get(i).x;
			y2 = points.get(i).y;
			
			d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
			curvature = Math.abs(t_angles[i]/d);
				
			
			if (max < curvature) {
				max = curvature;
				eMax = i;
			}
		}		
		if (max < TAIL)  // check whether max is greater than threshold TAIL or not
			eMax = points.size()-1;
		
		if (sMax == 0 && eMax == points.size()-1)
			return points;
		
		ArrayList<Point2D.Float> newp = new ArrayList<Point2D.Float>();
		for (i = sMax; i <= eMax; i++)
			newp.add(points.get(i));
		
		return newp;
	}

	public void turnedAngles(ArrayList<Point2D.Float> points, double[] t_angles) {
		
		float x1, y1, x2, y2, k;
		double a, a1 = 0, a2;
		
		for (int i = 0; i < points.size()-1; i++) {
			x1 = points.get(i).x;
			y1 = points.get(i).y;
			x2 = points.get(i+1).x;
			y2 = points.get(i+1).y;
			
			if (x1 != x2) {
				k = (y2 - y1) / (x1 - x2); // Slope between two adjacent points
				if (x1 > x2) {
					if (k >= 0)
						a2 = Math.atan(k) - Math.PI;
					else
						a2 = Math.atan(k) + Math.PI;
				}
				else
					a2 = Math.atan(k);
			}
			else {
				if (y1 > y2)
					a2 = Math.PI * 0.5;
				else
					a2 = -Math.PI * 0.5;
			}
			
			if (i != 0) {
				a = a1 - a2;
				if (a1 < 0 && a2 > 0 && (Math.PI+a1) < a2)
					t_angles[i] = a + 2 * Math.PI;
				else if (a1 > 0 && a2 < 0 && (Math.PI+a2) < a1)
					t_angles[i] = a - 2 * Math.PI;
				else
					t_angles[i] = a;
			}
			else
				t_angles[i] = a2;
			
			a1 = a2;
		}
	}

	public ArrayList<Integer> findCorners(ArrayList<Point2D.Float> points) {
		
		int i, j, k, c1, c2, iMax;
		float x1, y1, x2, y2, px, py;
		double md = 0, ratio, d1 = 0, d2, chd1 = 0, chd2;
		ArrayList<Integer> index = new ArrayList<Integer>();  // Store indices of corners
		double[] chord = new double[points.size() - 4];  // An array to store chords
		
		for (i = 2; i < points.size()-2; i++) {
			px = points.get(i).x;
			py = points.get(i).y;
			
			for (k = 1; (i-k) >= 0 && (i+k) < points.size(); k++) { // Find the best support region
				x1 = points.get(i-k).x;
				y1 = points.get(i-k).y;
				x2 = points.get(i+k).x;
				y2 = points.get(i+k).y;
				chd2 = Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2));  // The chord lengths of points
				d2 = Line2D.ptSegDist(x1, y1, x2, y2, px, py);  // The perpendicular distance of p to line(p1,p2)
				
				if (k != 1) {
					if (chd1 >= chd2)
						break;
					if (d1 > 0) {
						if (d1/chd1 >= d2/chd2)
							break;
					}
				}
				
				d1 = d2;
				chd1 = chd2;
			}
			chord[i-2] = chd1;
		}
		
		double[] ds = new double[chord.length];  // Find threshold of distance - the median of array chord
		System.arraycopy(chord, 0, ds, 0, chord.length);
		Arrays.sort(ds);
		if (ds.length%2 == 0)
			md = (ds[ds.length/2] + ds[ds.length/2 + 1])/2;
		else
			md = ds[ds.length/2];

		index.add(0);  // Store the index of the start point
		
		for (i = 2; i < (points.size() - 2); i++) {  // Find possible corners
			if (chord[i-2] < md) {
				index.add(i);
			}
		}
		
		index.add(points.size()-1);  // Store the index of the end point
		
		boolean repeat = true;  // Check if any corners can be added
		while(repeat) {
			repeat = false;
			
			for (i = 1; i < index.size(); i++) {
				c1 = index.get(i-1);
				c2 = index.get(i);
				ratio = notLine(points, c1, c2);
				
				if (ratio < T1) {  // Find an another points between c1 and c2
					d1 = Double.MIN_VALUE;
					iMax = Integer.MIN_VALUE;
					x1 = points.get(c1).x;
					y1 = points.get(c1).y;
					x2 = points.get(c2).x;
					y2 = points.get(c2).y;
					
					for (j = c1+1; j < c2; j++) {
						px = points.get(j).x;
						py = points.get(j).y;
						
						if(Math.abs(turnedAngle(x1, y1, x2, y2, px, py)) < Math.PI * 0.09)
							continue;
						
						d2 = Line2D.ptSegDist(x1, y1, x2, y2, px, py);  // The perpendicular distance of p to line(p1,p2)
						if (d1 < d2) {
							d1 = d2;
							iMax = j;
						}
					}

					if (iMax > 0) {
						index.add(i, iMax);
						repeat = true;
						break;
					}
				}
			}
		}
		
		ArrayList<Point2D.Float> temp = new ArrayList<Point2D.Float>();  // Store the possible corners
		double[] temp_a;  // Store the turned angles of possible corners
		
		repeat = true;  // Check if any corners can be removed
		while(repeat) {
			repeat = false;
			
			for (i = 0; i < index.size(); i++)
				temp.add(points.get(index.get(i)));
			temp_a = new double[index.size()-1];
			turnedAngles(temp, temp_a);
			
			j = 1;
			for (i = 1; i < index.size()-1; i++) {
				c1 = index.get(i-1);
				c2 = index.get(i+1);
				ratio = notLine(points, c1, c2);
				
				if (ratio >= T2 && Math.abs(temp_a[j]) < Math.PI * 0.13) {
					index.remove(i);  // Remove the point between c1 and c2
					i--;
					repeat = true;
				}
				j++;
			}
			temp.clear();
		}
		
		repeat = true;  // Double check if any corners are too close (especially for small shapes)
		while(repeat) {
			repeat = false;
			
			if (index.size() != 2) {
				for (i = 0; i < index.size(); i++)
					temp.add(points.get(index.get(i)));
				temp_a = new double[index.size()-1];
				turnedAngles(temp, temp_a);
				
				j = 1;
				for (i = 1; i < index.size() && index.size() > 2; i++) {
					c1 = index.get(i-1);
					c2 = index.get(i);
					x1 = points.get(c1).x;
					y1 = points.get(c1).y;
					x2 = points.get(c2).x;
					y2 = points.get(c2).y;
					d1 = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
					
					if (d1 < 30) {
						if (i == index.size()-1) {
							if (d1/perimeter < 0.05 || Math.abs(temp_a[i-1]) < Math.PI * 0.5) {
								index.remove(i-1);
								repeat = true;
								break;
							}
						}
						else if (i == 1) {
							if (d1/perimeter < 0.05 || Math.abs(temp_a[i]) < Math.PI * 0.5) {
								index.remove(i);
								repeat = true;
								break;
							}
						}
						else {
							if (Math.abs(temp_a[i]) < Math.abs(temp_a[i-1])) {
								if (d1/perimeter < 0.05 || Math.abs(temp_a[i]) < Math.PI * 0.5) {
									index.remove(i);
									repeat = true;
									break;
								}
							}
							else {
								if (d1/perimeter < 0.05 || Math.abs(temp_a[i-1]) < Math.PI * 0.5) {
									index.remove(i-1);
									repeat = true;
									break; 
								}
							}
						}
					}
					j++;
				}
				temp.clear();
			}
		}
		
		return index;
	}
	
	public double notLine(ArrayList<Point2D.Float> points, int c1, int c2) {
		float x1, y1, x2, y2;
		double ed, pd = 0;
		
		x1 = points.get(c1).x;
		y1 = points.get(c1).y;
		x2 = points.get(c2).x;
		y2 = points.get(c2).y;
		
		ed = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));  // Distance between c1 and c2
		
		for (int i = c1; i < c2; i++) {
			x1 = points.get(i).x;
			y1 = points.get(i).y;
			x2 = points.get(i+1).x;
			y2 = points.get(i+1).y;
			pd += Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));  // Path length between c1 and c2
		}
		
		return (ed/pd);
	}
	
	public double turnedAngle(float x1, float y1, float x2, float y2, float px, float py) {
		
		float k;
		double a, a1 = 0, a2;
		
		if (x1 != px) {
			k = (py - y1) / (x1 - px); // Slope between p1 and p
			if (x1 > x2) {
				if (k >= 0)
					a1 = Math.atan(k) - Math.PI;
				else
					a1 = Math.atan(k) + Math.PI;
			}
			else
				a1 = Math.atan(k);
		}
		else {
			if (y1 > py)
				a1 = Math.PI * 0.5;
			else
				a1 = -Math.PI * 0.5;
		}
		
		if (px != x2) {
			k = (y2 - py) / (px - x2); // Slope between p and p2
			if (px > x2) {
				if (k >= 0)
					a2 = Math.atan(k) - Math.PI;
				else
					a2 = Math.atan(k) + Math.PI;
			}
			else
				a2 = Math.atan(k);
		}
		else {
			if (py > y2)
				a2 = Math.PI * 0.5;
			else
				a2 = -Math.PI * 0.5;
		}
		
		a = a1 - a2;
		if (a1 < 0 && a2 > 0 && (Math.PI+a1) < a2)
			a = a + 2 * Math.PI;
		else if (a1 > 0 && a2 < 0 && (Math.PI+a2) < a1)
			a = a - 2 * Math.PI;

		return a;
	}
	
	public boolean isCurved(ArrayList<Point2D.Float> points, ArrayList<Integer> indices) {
		
		float x1, y1, x2, y2;
		double chd, d, dMax;
		int i, j, c1, c2, iMax = 0;
		boolean curve = false;

		for (i = 1; i < indices.size(); i++) {  // Check if any corners are removed incorrectly
			c1 = indices.get(i-1);
			c2 = indices.get(i);
			x1 = points.get(c1).x;
			y1 = points.get(c1).y;
			x2 = points.get(c2).x;
			y2 = points.get(c2).y;
			chd = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
			dMax = Double.MIN_VALUE;
			
			for (j = c1+1; j < c2; j++) {
				d = Line2D.ptLineDist(x1, y1, x2, y2, points.get(j).x, points.get(j).y);
				if(dMax < d) {
					dMax = d;
					iMax = j;
				}
			}
			
			if (dMax > DIS || dMax/chd > DIST ) {
				float px = points.get(iMax).x;
				float py = points.get(iMax).y;
				double d1 = Math.sqrt((x1 - px) * (x1 - px) + (y1 - py) * (y1 -py));
				double d2 = Math.sqrt((x2 - px) * (x2 - px) + (y2 - py) * (y2 -py));
				
				if (d1 > 0.05*perimeter && d2 > 0.05*perimeter) {  // Do not add too close point
					indices.add(i, iMax);
					i--;
					curve = true;
				}
			}
		}
		
		if (!curve)
			return false;
		else
			return true;
	}
	
	public boolean isopen(ArrayList<Point2D.Float> points, ArrayList<Integer> indices) {
		
		float x1 = points.get(0).x;
		float y1 = points.get(0).y;
		float x2 = points.get(points.size()-1).x;
		float y2 = points.get(points.size()-1).y;
		
		double d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
		d = d/perimeter;
		
		if (d < OPEN)
			return false;
		else
			return true;
	}

	public Lines constructLine (ArrayList<Point2D.Float> points) {
		int n = JOptionPane.showConfirmDialog(
			    null,
			    "Static State?",
			    "Static Property",
			    JOptionPane.YES_NO_OPTION);
		
		if(n==0)		
			l = new Lines(points.get(0).x, points.get(0).y, points.get(points.size()-1).x, points.get(points.size()-1).y, true);
		else if(n==1)
			l = new Lines(points.get(0).x, points.get(0).y, points.get(points.size()-1).x, points.get(points.size()-1).y, false);
		
		return l;
	}
	
	public boolean isArrow(ArrayList<Point2D.Float> corners, double[] corners_a) {
		
		for (int i = 1; i < corners_a.length-1; i++) {
			if (Math.abs(corners_a[i]) > Math.PI * 0.6)
				if (Math.abs(corners_a[i+1]) > Math.PI * 0.88) {
					
					float x2 = corners.get(i).x;
					float y2 = corners.get(i).y;
					float x3 = corners.get(i+2).x;
					float y3 = corners.get(i+2).y;
					double d = Math.sqrt((x2 - x3) * (x2 - x3) + (y2 - y3) * (y2 - y3));
					
					if (d < 30) {
						double angle;
						float x1 = corners.get(i-1).x;
						float y1 = corners.get(i-1).y;
						
						//d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) / 5;
						//if (d > 12)
						d = 12;
						
						int[] xpoints = {Math.round(x1), Math.round(x2), 0, Math.round(x2), 0};
						int[] ypoints = {Math.round(y1), Math.round(y2), 0, Math.round(y2), 0};
						
						if (x1 != x2)
							angle = Math.abs(Math.atan((y1 - y2) / (x1 - x2)));
						else
							angle = Math.PI * 0.5;
						
						float dx1 = (float) (d * Math.cos(angle - Math.PI / 6));
						float dy1 = (float) (d * Math.sin(angle - Math.PI / 6));
						float dx2 = (float) (d * Math.sin(Math.PI / 3 - angle));
						float dy2 = (float) (d * Math.cos(Math.PI / 3 - angle));
						
						if (x1 >= x2) {
							xpoints[2] = Math.round(x2 + dx1);
							xpoints[4] = Math.round(x2 + dx2);
							if (y1 < y2) {
								ypoints[2] = Math.round(y2 - dy1);
								ypoints[4] = Math.round(y2 - dy2);
							}
							else {
								ypoints[2] = Math.round(y2 + dy1);
								ypoints[4] = Math.round(y2 + dy2);
							}
						}
						
						else if (x1 < x2) {
							xpoints[2] = Math.round(x2 - dx1);
							xpoints[4] = Math.round(x2 - dx2);
							if (y1 < y2) {
								ypoints[2] = Math.round(y2 - dy1);
								ypoints[4] = Math.round(y2 - dy2);
							}
							else {
								ypoints[2] = Math.round(y2 + dy1);
								ypoints[4] = Math.round(y2 + dy2);
							}
						}
						
						a = new Arrows(xpoints, ypoints);
						return true;
					}
				}
		}
		return false;
	}
	
	public boolean isDatastores(ArrayList<Point2D.Float> corners, double[] corners_a) {
		float x1 = corners.get(0).x;
		float y1 = corners.get(0).y;
		float x2 = corners.get(1).x;
		float y2 = corners.get(1).y;
		
		if (Math.abs((y1 - y2)/(x1 - x2)) < 1)
		    if (corners_a[1] * corners_a[2] > 0)
		    	if (Math.abs(corners_a[1]+corners_a[2]) > Math.PI * 0.8 && Math.abs(corners_a[1]+corners_a[2]) < Math.PI * 1.2) {
		    		int[] xpoints = new int[4];
		    		int[] ypoints = new int[4];
		    		xpoints[0] = Math.round(x1);
		    		ypoints[0] = Math.round(y1);
		    		xpoints[1] = Math.round(x2);
		    		ypoints[1] = ypoints[0];
		    		xpoints[2] = xpoints[1];
		    		ypoints[2] = Math.round(corners.get(2).y);
		    		xpoints[3] = xpoints[0];
		    		ypoints[3] = ypoints[2];
		    		
		    		ds = new Datastores(xpoints, ypoints);
		    		
		    		return true;
		    	}
					
		return false;
	}
	
	public Rectangles constructRectangle(Polygon sh, ArrayList<Point2D.Float> corners, double[] corners_a) {
		
		int i, j, rightangles = 0;
		float x1, y1, x2, y2, width = 0, height = 0;
		double[] sides = new double[4];
		 int n;
		
		// Check if have 3 right-angles
		for (i = 1; i < corners_a.length; i++) {
			if (Math.abs(corners_a[i]) > Math.PI * 0.5 * 0.8 && Math.abs(corners_a[i]) < Math.PI * 0.5 * 1.2) {
				x1 = corners.get(i).x;
				y1 = corners.get(i).y;
				if (i != 1) {
					if (Math.abs(corners_a[i-1]) < Math.PI * 0.2) {
						x2 = corners.get(i-2).x;
						y2 = corners.get(i-2).y;
					}
				}
				x2 = corners.get(i-1).x;
				y2 = corners.get(i-1).y;
				sides[rightangles] = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
				rightangles++;
			}
		}
		
		if (rightangles < 3)
			return null;
		
		x1 = corners.get(corners.size()-1).x;
		y1 = corners.get(corners.size()-1).y;
		x2 = corners.get(corners.size()-2).x;
		y2 = corners.get(corners.size()-2).y;
		
		if (rightangles == 3) {
			sides[3] = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
		}
		else if (rightangles == 4) {
			sides[0] += Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
		}
		
		if (((sides[1] < sides[2])?(sides[1]/sides[2]):(sides[2]/sides[1])) > 0.8) {  // Equal sides
			double side = 0;
			for (j = 0; j < sides.length; j++)
				side += sides[j];
			side = side/4;
			
			n = JOptionPane.showConfirmDialog(
				    null,
				    "Static State?",
				    "Static Property",
				    JOptionPane.YES_NO_OPTION);
			
			if(n==0){
				r= new Rectangles(sh.getBounds().x, sh.getBounds().y, (int)Math.round(side), (int)Math.round(side), true);
			}
			else if(n==1){
				r= new Rectangles(sh.getBounds().x, sh.getBounds().y, (int)Math.round(side), (int)Math.round(side), false);
			}
			
			
		}
		else {
			width = (float) ((sides[0] + sides[2]) / 2);
			height = (float) ((sides[1] + sides[3]) / 2);
			if (Math.abs(corners_a[0]) > 1.25 && Math.abs(corners_a[0]) < 1.8){
				n = JOptionPane.showConfirmDialog(
					    null,
					    "Static State?",
					    "Static Property",
					    JOptionPane.YES_NO_OPTION);
				if(n==0){
					r= new Rectangles(sh.getBounds().x, sh.getBounds().y, Math.round(width), Math.round(height), true);
				}
				else if(n==1){
					r= new Rectangles(sh.getBounds().x, sh.getBounds().y, Math.round(width), Math.round(height), false);
				}	
				
				
			}
			else
				n = JOptionPane.showConfirmDialog(
					    null,
					    "Static State?",
					    "Static Property",
					    JOptionPane.YES_NO_OPTION);
			if(n==0){
				r= new Rectangles(sh.getBounds().x, sh.getBounds().y, Math.round(height), Math.round(width), true);
			}
			else if(n==1){
				r= new Rectangles(sh.getBounds().x, sh.getBounds().y, Math.round(height), Math.round(width),  false);
			}	
					
		}
		return r;
	}
	
	public boolean isParallelogram(ArrayList<Point2D.Float> corners, double[] corners_a) {
		
		double angle = Math.abs(corners_a[1] + corners_a[2]);
		if (angle > Math.PI * 0.8 && angle < Math.PI * 1.2) {
			if (((Math.abs(corners_a[1]) < Math.abs(corners_a[3]))?(corners_a[1]/corners_a[3]):(corners_a[1])) > 0.9) {
				int[] xpoints = new int[4];
				int[] ypoints = new int[4];
				
				for (int i = 0; i < 3; i++) {
					xpoints[i] = Math.round(corners.get(i).x);
					ypoints[i] = Math.round(corners.get(i).y);
				}
				
				xpoints[3] = xpoints[2] + (xpoints[0] - xpoints[1]);
				ypoints[3] = ypoints[2] + (ypoints[0] - ypoints[1]);
				
				po = new Parallelograms(xpoints, ypoints);
				
				return true;
			}
		}
		
		return false;
	}
	
	public Polygons constructPolygon(ArrayList<Point2D.Float> corners) {
		
		int[] xpoints = new int[corners.size()-1];
		int[] ypoints = new int[corners.size()-1];
		
		for (int i = 0; i < corners.size()-1; i++) {
			xpoints[i] = Math.round(corners.get(i).x);
			ypoints[i] = Math.round(corners.get(i).y);
		}
		
		pg = new Polygons(xpoints, ypoints, xpoints.length, true);
		return pg;
		//System.out.println("Polygon");
	}
	
	public Circles constructEllipse(Polygon s, ArrayList<Point2D.Float> corners, double[] corners_a) {
		
		int n;
		int iMax = 0, jMax = 0;
		float x1, y1, x2, y2;
		float k = java.lang.Float.MIN_VALUE;
		double d;  
		double a = Double.MIN_VALUE;
		double b;
		
		for (int i = 0; i < corners.size()-1; i++) {  // The longest diameter
			x1 = corners.get(i).x;
			y1 = corners.get(i).y;
			
			for (int j = i+1; j < corners.size(); j++) {
				x2 = corners.get(j).x;
				y2 = corners.get(j).y;
				
				d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 -y2));
				
				if (a < d) {
					a = d;  // The long axis
					iMax = i;
					jMax = j;
				}
			}
		}
		
		b = 2 * (perimeter - 2 * a) / (2 * Math.PI - 4);  // The short axis
		
		n = JOptionPane.showConfirmDialog(
			    null,
			    "Static State?",
			    "Static Property",
			    JOptionPane.YES_NO_OPTION);
		
		if (b/a > 0.9) {  //b > a * 0.8 && b < a * 1.2
			if(n==0)
				c = new Circles((float)s.getBounds2D().getX(), (float)s.getBounds2D().getY(), (float)(a+b)/2, true);
			else if(n==1)
				c = new Circles((float)s.getBounds2D().getX(), (float)s.getBounds2D().getY(), (float)(a+b)/2, false);
			return c;
		}
		else {
			if (corners.get(iMax).x != corners.get(jMax).x)
				k = (corners.get(iMax).y - corners.get(jMax).y) / (corners.get(iMax).x - corners.get(jMax).x);
			
			if (k > -1 && k < 1){
				
				if(n==0)
				{
					c = new Circles((float)s.getBounds2D().getX(), (float)s.getBounds2D().getY(), (float)(a+b)/2, true);
				}
				
				else if(n==1)
				{
					c = new Circles((float)s.getBounds2D().getX(), (float)s.getBounds2D().getY(), (float)(a+b)/2, false);
				}
			}
			else{
				if(n==0)
					c = new Circles((float)s.getBounds2D().getX(), (float)s.getBounds2D().getY(), (float)(a+b)/2, true);
				else if(n==1)
					c = new Circles((float)s.getBounds2D().getX(), (float)s.getBounds2D().getY(), (float)(a+b)/2, false);
			}
			return c; 
		}
	}
	
	public boolean isQuadCurve(Polygon s, ArrayList<Point2D.Float> corners, double[] corners_a) {
		
		int iMax = 0;
		double d, dMax = Double.MIN_VALUE;
		float x1 = corners.get(0).x;
		float y1 = corners.get(0).y;
		float x2 = corners.get(corners.size()-1).x;
		float y2 = corners.get(corners.size()-1).y;

		for (int i = 1; i < corners.size()-1; i++) {
			float px = corners.get(i).x;
			float py = corners.get(i).y;
			
			d = Line2D.ptSegDist(x1, y1, x2, y2, px, py);
			
			if (dMax < d) {
				dMax = d;
				iMax = i;
			}
		}
		
		if (x1 != x2) {
			double k = (y2 - y1) / (x1 - x2);
			
			if (k > 0) {
				if (Line2D.relativeCCW(x1, y1, x2, y2, corners.get(iMax).x, corners.get(iMax).y) == ((x1 < x2)?1:-1))
					qc = new QuadCurves(x1, y1, (float)s.getBounds2D().getMinX(), (float)s.getBounds2D().getMinY(), x2, y2);
				else
					qc = new QuadCurves(x1, y1, (float)s.getBounds2D().getMaxX(), (float)s.getBounds2D().getMaxY(), x2, y2);
			}
			else if (k < 0){
				if (Line2D.relativeCCW(x1, y1, x2, y2, corners.get(iMax).x, corners.get(iMax).y) == ((x1 < x2)?1:-1))
					qc = new QuadCurves(x1, y1, (float)s.getBounds2D().getMaxX(), (float)s.getBounds2D().getMinY(), x2, y2);
				else
					qc = new QuadCurves(x1, y1, (float)s.getBounds2D().getMinX(), (float)s.getBounds2D().getMaxY(), x2, y2);
			}
			else {
				if (Line2D.relativeCCW(x1, y1, x2, y2, corners.get(iMax).x, corners.get(iMax).y) == ((x1 < x2)?1:-1))
					qc = new QuadCurves(x1, y1, corners.get(iMax).x, corners.get(iMax).y - 50, x2, y2);
				else
					qc = new QuadCurves(x1, y1, corners.get(iMax).x, corners.get(iMax).y + 50, x2, y2);
			}
		}
		else {
			if (Line2D.relativeCCW(x1, y1, x2, y2, corners.get(iMax).x, corners.get(iMax).y) == ((y1 < y2)?-1:1))
				qc = new QuadCurves(x1, y1, corners.get(iMax).x - 50, corners.get(iMax).y, x2, y2);
			else
				qc = new QuadCurves(x1, y1, corners.get(iMax).x + 50, corners.get(iMax).y, x2, y2);
		}
		System.out.println("QuadCurve");
		return true;
	}
	
	public boolean isCubicCurve(ArrayList<Point2D.Float> corners, double[] corners_a) {
		
		int i, j = 0, iMax1 = 0;
		boolean wave = false;
		double d, dMax1 = Double.MIN_VALUE, dMax2 = Double.MIN_VALUE;
		
		for (i = 2; i < corners_a.length; i++) {
			if (corners_a[1] * corners_a[i] < 0) {
				wave = true;
				j = i;
			}
		}
		
		if (!wave)
			return false;
		
		float x1 = corners.get(0).x;
		float y1 = corners.get(0).y;
		float x2 = corners.get(corners.size()-1).x;
		float y2 = corners.get(corners.size()-1).y;
		float px, py;
		int locat = 0;
		
		for (i = 1; i < j; i++) {
			px = corners.get(i).x;
			py = corners.get(i).y;
			
			if (i == 1)
				locat = Line2D.relativeCCW(x1, y1, x2, y2, px, py);
			else
				if (Line2D.relativeCCW(x1, y1, x2, y2, px, py) != locat) {
					j = i;
					break;
				}
			
			d = Line2D.ptSegDist(x1, y1, x2, y2, px, py);
			
			if (dMax1 < d) {
				dMax1 = d;
				iMax1 = i;
			}
		}
		
		for (i = j; i < corners.size()-1; i++) {
			px = corners.get(i).x;
			py = corners.get(i).y;
			
			d = Line2D.ptSegDist(x1, y1, x2, y2, px, py);
			
			if (dMax2 < d) {
				dMax2 = d;
			}
		}
		
		double k = (corners.get(iMax1).y - y1) / (x1 - corners.get(iMax1).x);
		d = Math.sqrt((x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2)) / 4;
		double dMax = (float) (dMax1 + dMax2) / 2 * 1.2;
		if (k > 0) {
			if (corners_a[1] > 0) {
				if (x1 < x2)
					cc = new CubicCurves(x1, y1, x1+(float)d, y1-(float)dMax, x1+3*(float)d, y1+(float)dMax, x1+4*(float)d, y1);
				else
					cc = new CubicCurves(x1, y1, x1-(float)d, y1+(float)dMax, x1-3*(float)d, y1-(float)dMax, x1-4*(float)d, y1);
			}
			else {
				if (y1 < y2)
					cc = new CubicCurves(x1, y1, x1-(float)dMax, y1+(float)d, x1+(float)dMax, y1+3*(float)d, x1, y1+4*(float)d);
				else
					cc = new CubicCurves(x1, y1, x1+(float)dMax, y1-(float)d, x1-(float)dMax, y1-3*(float)d, x1, y1-4*(float)d);
			}
		}
		else {
			if (corners_a[1] < 0) {
				if (x1 < x2)
					cc = new CubicCurves(x1, y1, x1+(float)d, y1+(float)dMax, x1+3*(float)d, y1-(float)dMax, x1+4*(float)d, y1);
				else
					cc = new CubicCurves(x1, y1, x1-(float)d, y1-(float)dMax, x1-3*(float)d, y1+(float)dMax, x1-4*(float)d, y1);
			}
			else {
				if (y1 < y2)
					cc = new CubicCurves(x1, y1, x1+(float)dMax, y1+(float)d, x1-(float)dMax, y1+3*(float)d, x1, y1+4*(float)d);
				else
					cc = new CubicCurves(x1, y1, x1-(float)dMax, y1-(float)d, x1+(float)dMax, y1-3*(float)d, x1, y1-4*(float)d);
			}
		}
		
		System.out.println("CubicCurve");
		
		return true;
	}
}