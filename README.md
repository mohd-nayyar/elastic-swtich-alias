Hi there,\
This project shows how you can switch alias in elasticsearch.\
I have created two elastic alias alias1 pointing to elastic index index1 and alias2 pointing to elastic index index2.\
alias1 -> index1\
alias2 -> index2\
</br>

now I need to switch these alias as</br>
alias1 -> index2\
alias2 -> index1</br>

**Work-flow:**</br>
0. Run this spring-boot application on port 8080.
1. Run an elastic instance on local running on port 9200.
2. Create two elastic indices named as index1 and index2.
   1. <code>curl -X PUT "localhost:9200/index1?pretty"</code>
   2. <code>curl -X PUT "localhost:9200/index2?pretty"</code>
3. Create two elastic aliases alias1 and alias2 pointing to these indices.
   1. <code>curl -X POST "localhost:9200/_aliases?pretty" -H 'Content-Type: application/json' -d'
      {
      "actions" : [
      { "add" : { "index" : "index1", "alias" : "alias1" } },
      { "add" : { "index" : "index2", "alias" : "alias2" } }

   ]
   }
   '</code>
4. Check the alias and index using below command.</br>
   <code>curl -X GET "localhost:9200/_alias/?pretty"</code>
5. Run the switches alias curl to switch aliases for indices.</br>
   <code>
   curl -X POST "localhost:9200/_aliases?pretty" -H 'Content-Type: application/json' -d'
   {
   "actions" : [
   { "remove" : { "index" : "index1", "alias" : "alias1" } },
   { "add" : { "index" : "index2", "alias" : "alias1l" } },
   { "remove" : { "index" : "index2", "alias" : "alias2" } },
   { "add" : { "index" : "index1", "alias" : "alias2" } }

   ]
   }
   </code>
6. Again check the alias and index using below command.</br>
   <code>curl -X GET "localhost:9200/_alias/?pretty"</code>
