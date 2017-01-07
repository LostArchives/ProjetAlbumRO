# Projet Album RO

This project was about to create an automatic album generator which would use data relative to a group of photo to create an optimized way of
ordering by using algorithms such as HillClimber First improvement or Iterative Local Search.

The following criteria are actually supported :

	-HASH
	-PHASH
	-DHASH
	-COLORS
	-COMMON_TAGS
	-UNCOMMON_TAGS
	-NB_UNCOMMON_TAGS
	-GREY_AVG


#Project Structure

Packages :


	There are three packages :
		The package models with classes not related to algorithms :
		-AlbumPhoto
		-AlbumCriteria (enum with all possible criteria you can use to evaluate your album)
		-AlgorithmManager (singleton class able to manage any algorithm children of baseAlgorithm)
		-FileWriter (class able to create and write files -> used to write solution sole at the end of an algorithm)
		
	The package models.algorithms with classes related to algorithms :
		-BaseAlgorithm : an abstract class for any algorithm
		-HillClimberFirst : one type of algorithm child of the abstract class
		-IterativeLocalSearch : another type of algorithm child of the abstract class
		
	The package test to do any type of Test (it already contains a class Test with some examples)

Folders :


The data folder contains json files with all the data relative to the album photo

The results folder contains the different solutions created by your algorithms

The html folder contains the album photo pictures (in the img subfolder) and html files


#Installation

The python script is automatically executed by the java program but you need to have python installed on your machine. You can download it here :
https://www.python.org/downloads/

After that , you just have to copy the project into your Eclipse Workspace and import it