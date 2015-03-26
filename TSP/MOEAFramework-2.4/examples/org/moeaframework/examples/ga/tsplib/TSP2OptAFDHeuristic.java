/* Copyright 2009-2015 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.moeaframework.examples.ga.tsplib;

/**
 * Implementation of the 2-opt heuristic for the traveling salesman problem.
 * The 2-opt heuristic searches for any two edges in a tour that can be
 * rearranged to produce a shorter tour.  For example, a tour with any edges
 * that intersect can be shortened by removing the intersection.
 */
public class TSP2OptAFDHeuristic {
	
	/**
	 * The traveling salesman problem instance.
	 */
	private final TSPInstance instance;

	/**
	 * Constructs a new 2-opt heuristic for the specified traveling salesman
	 * problem instance.
	 * 
	 * @param instance the traveling salesman problem instance
	 */
	public TSP2OptAFDHeuristic(TSPInstance instance) {
		super();
		this.instance = instance;
	}
	/**
	 * Applies the Greedy Algorithm heuristic to the specified tour.
	 * 
	 * @param tour the tour that is modified by the Greedy Algorithm heuristic
	 */
	public void applyGreedy(Tour tour) {
		DistanceTable distanceTable = instance.getDistanceTable();
		
		// tours with 2 or fewer nodes are already optimal
		if (tour.size() < 3) {
			return;
		}
		
			
			for (int i = 0; i < tour.size()-1; i++) {
					double d1 = distanceTable.getDistanceBetween(tour.get(i), tour.get(i+1));
					double d2 = distanceTable.getDistanceBetween(tour.get(i), tour.get(i+2));
					
					// if distance can be shortened, adjust the tour
					if (d2 < d1) {
						tour.reverse(i+1, i+2);
					}

			}

	}
	
	/**
	 * Applies the AFD + 2-opt heuristic to the specified tour.
	 * 
	 * @param tour the tour that is modified by the 2-opt heuristic
	 */
	public void apply(Tour tour) {
		DistanceTable distanceTable = instance.getDistanceTable();
		boolean modified = true;
		
		// Applies the AFD heuristic to the specified tour.
		int toursize = tour.size();
		double[][] dmaxsum;
		dmaxsum = new double[toursize][2];
		double[][][] dArray;
		dArray = new double[toursize][toursize][3];

		for (int i = 0; i < toursize; i++) {
			for (int j = 0; j < toursize; j++) {
				dArray[i][j][0] = distanceTable.getDistanceBetween(tour.get(i), tour.get(j));
				if (dmaxsum[i][0] < dArray[i][j][0])
					dmaxsum[i][0] = dArray[i][j][0];
				dmaxsum[i][1] += dArray[i][j][0];
			}
			for (int j = 0; j < toursize; j++) {
				if (dmaxsum[i][0]== 0)
				   dArray[i][j][1] = 1;
				else
				   dArray[i][j][1] = 1 - (dArray[j][1][0]/dmaxsum[i][0]);
				
				if (dmaxsum[i][1]== 0)
				   dArray[j][1][2] = 1;				
				else
				   dArray[i][j][2] = 1 - (dArray[j][1][0]/dmaxsum[i][1]);			
			}
		}

		// tours with 3 or fewer nodes are already optimal
		if (tour.size() < 4) {
			return;
		}
		
		while (modified) {
			modified = false;
			int inowp = 0;
			int jnowp = 0;
			for (int i = 0; i < tour.size(); i++) {
				for (int j = i+2; j < tour.size(); j++) {
					double d1 = distanceTable.getDistanceBetween(tour.get(i), tour.get(i+1)) +
							distanceTable.getDistanceBetween(tour.get(j), tour.get(j+1));
					double d2 = distanceTable.getDistanceBetween(tour.get(i), tour.get(j)) +
							distanceTable.getDistanceBetween(tour.get(i+1), tour.get(j+1));
					if (i+1 == tour.size())
						inowp = 0;
					else
						inowp = i+1;	
					
					if (j+1 == tour.size())
						jnowp = 0;
					else
						jnowp = j+1;		
					// if distance can be shortened, adjust the tour
					//if ((d2 < d1) && (dArray[i][j][1] + dArray[i+1][j+1][1] >= 1.2)){
					if ((d2 < d1) && (dArray[i][j][1] + dArray[inowp][jnowp][1] > 0.9)){
					//if (d2 < d1){
						tour.reverse(i+1, j);
						modified = true;
					}
				}
			}
		}
	}

}
