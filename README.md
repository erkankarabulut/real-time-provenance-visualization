# Real-time PROV-O Visualization

This is a Cytoscape[1] plugin that provides real-time PROV-O[2] data visualization capabilities. It is developed as part of my undergraduate student assistantship at the Software Quality R&D Lab, Yildiz Technical University[3], between 2016 and 2018.

The main goal of the project is; i) to convert OPM[4] data to PROV-O, ii) to provide an automated way to visualize the PROV-O data as a directed graph, iii) and run stastical as well as learning-based algorithms on the graph data for provenance data summarization and comparison tasks[A].

## Features
- OPM to PROV-O conversion
- Visualizing PROV-O data as a directed graph on Cytoscape
- Visualization-related features such as show/hide/highlight nodes and edges, applying different visual style templates, group by node type, sort nodes with the type "activity" by time, sort entities based on activity time, show/hide node properties
- Running provenance life cycle as a video based on activity timestamps
- Summarizing provenance data using an enhanced version of K-Means
- Comparing provenance data graphs for detecting similarities or finding anomalies

## How to Run? (2018)

### Requirement(s)

An XSLT Engine named Saxon is required to transform OPM files to PROV-O. You can download Saxon using the following link: http://saxon.sourceforge.net/

### Creating a Jar File 
1.	Open the project folder with a Java IDE.
2.	Add Maven dependencies to the Project which is given in the dependencies file of the project.
	-	If you do not have Maven you can download and install by following the instructions here: https://maven.apache.org/install.html
	-	For adding a dependency to Maven run this command at console sceen with filling the inside of quotation marks.

	mvn install:install-file -Dfile=”Jar File’s Path” -DgroupId=”GroupID” -DartifactId=”ArtifactId” -Dversion=”version” -Dpackaging=jar
 	

	-	After installing .jar files which is in dependencies files, add below code to pom.xml file.

	<dependency>
      		<groupId> groupId e</groupId>
      		<artifactId> artifactId </artifactId>
      		<version> version </version>
	 </dependency>

3.	After adding Maven dependencies, now you can create a jar file. For doing this in IntelliJ idea in the top menü follow “File – Project Structure” path.
4.	Choose the Artifacts under the Project Settings and click "+" plus icon.
5.	Under the Add tab follow the "JAR - From modules with dependencies" path.
6.	Click Ok and after choosing Output Directory (where JAR file will be create) click Ok buton again.

After following the steps above Jar file will be created in the directory that you choosed.

### Installing Jar File To The Cytoscape
Now you will learn how to install the jar file that you create with the applying above instructions to the Cytoscape.

1.	In the Cytoscape interface click the “Apps” part which is located in top menu and then choose “Apps Manager” tab.
2.	Under the “Install Apps” tab click “Install From File” button and choose the jar file that you created.
3.	After the installation you should see "Installed" status under the “Currently Installed” tab.

If you see any other thing than the "Installed" message,  open the JAR file (you can open with a program like Winrar) and change the MANIFEST.MF files under the 
META-INF folder with the MANIFEST.MF file that is in the APP folder which is again in the META-INF folder of the CytoVisProject’s documents.

### Related Publication(s)
[A] Yazici, Ilkay Melek, Erkan Karabulut, and Mehmet S. Aktas. "A data provenance visualization approach." 2018 14th International Conference on Semantics, Knowledge and Grids (SKG). IEEE, 2018.

### References
[1] Cytoscape, visited on 15.02.2023. 2001. url: https://cytoscape.org/

[2] PROV-O: The PROV Ontology, visited on 15.02.2023. 2013. url: https://www.w3.org/TR/prov-o/

[3] Software Quality Lab, Yildiz Technical University, visited on 15.02.2023. link: http://softwarequality.yildiz.edu.tr/

[4] The OPM Provenance Model (OPM), visited on 15.02.2023. url: https://openprovenance.org/opm/
