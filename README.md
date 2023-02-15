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


### Related Publication(s)
[A] Yazici, Ilkay Melek, Erkan Karabulut, and Mehmet S. Aktas. "A data provenance visualization approach." 2018 14th International Conference on Semantics, Knowledge and Grids (SKG). IEEE, 2018.

### References
[1] Cytoscape, visited on 15.02.2023. 2001. url: https://cytoscape.org/

[2] PROV-O: The PROV Ontology, visited on 15.02.2023. 2013. url: https://www.w3.org/TR/prov-o/

[3] Software Quality Lab, Yildiz Technical University, visited on 15.02.2023. link: http://softwarequality.yildiz.edu.tr/

[4] The OPM Provenance Model (OPM), visited on 15.02.2023. url: https://openprovenance.org/opm/
