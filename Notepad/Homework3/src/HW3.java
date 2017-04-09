import java.io.*;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.FileManager;

public class HW3 {


	static String defaultNameSpace = "http://org.semwebprogramming/chapter2/people#";
	static Model model = null;


	public static void main(String[] args) {

		InputStream in = null;

		try {

		   in = new FileInputStream(new File("seinfeld.rdf"));


		// Create an empty in-memory model and populate it from the graph
//		model = ModelFactory.createMemModelMaker().createModel(null);
		model = ModelFactory.createOntologyModel();
		model.read(in,defaultNameSpace); // null base URI, since model URIs are absolute
		in.close();


	   } catch (Exception e) {

          e.printStackTrace();
       }

		// ADD YOUR QUERY IN THE LINES BELOW. YOU SHOULD NOT HAVE TO CHANGE THE NAMESPACE PREFIXES
	/*			String queryString =
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
								"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
							    "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
							    "PREFIX seinfeld: <http://www.seinfeld.com/seinfeld/> " +
								"PREFIX ppl: <http://www.randompeople.com/ppl/> " +
								"PREFIX job: <http://www.job.com/job/> " +

								"SELECT ?subj ?pred ?obj " +
								"WHERE {" +
								"      ?subj ?pred ?obj . " +
								"      }";

		        System.out.println("Issuing query #1....");

				//call this method to execute your SPARQL query
				issueSPARQL(queryString);

*/
		// ADD YOUR QUERY IN THE LINES BELOW. YOU SHOULD NOT HAVE TO CHANGE THE NAMESPACE PREFIXES
		String queryString =
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
					    "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
					    "PREFIX seinfeld: <http://www.seinfeld.com/seinfeld/> " +
						"PREFIX ppl: <http://www.randompeople.com/ppl/> " +
						"PREFIX job: <http://www.job.com/job/> " +
						"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +

						"SELECT ?Subject " +
						"WHERE {" + 
						"     ?Subject ppl:hasAge ?age . " +
						"     FILTER(xsd:integer(?age)>30) ." +
						"      }";

        System.out.println("Issuing query #1.... ");

		//call this method to execute your SPARQL query
		issueSPARQL(queryString);


	    //Add your second query
		String queryString2 =
				"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
						"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
					    "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
					    "PREFIX seinfeld: <http://www.seinfeld.com/seinfeld/> " +
						"PREFIX ppl: <http://www.randompeople.com/ppl/> " +
						"PREFIX job: <http://www.job.com/job/> " +

						"SELECT ?Surname " +
						"WHERE {" +
						"      <seinfeld:George> foaf:knows ?person . " +
						"  		?person foaf:surname ?Surname ." +
						"       }";

        System.out.println("Issuing query #2....");

		//call this method to execute your SPARQL query
		issueSPARQL(queryString2);

        //...fill in the rest of your queries
		
		//Add your third query
				String queryString3 =
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
								"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
							    "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
							    "PREFIX seinfeld: <http://www.seinfeld.com/seinfeld/> " +
								"PREFIX ppl: <http://www.randompeople.com/ppl/> " +
								"PREFIX job: <http://www.job.com/job/> " +
								"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +

								"SELECT ?Surname ?Age " +
								"WHERE {" +
								"      ?person rdf:type <foaf:Person> ;" +
								"  		foaf:surname ?Surname ;" +
								"		ppl:hasAge ?Age ." +
								"       }";

		        System.out.println("Issuing query #3....");

				//call this method to execute your SPARQL query
				issueSPARQL(queryString3);
				
				//Add your second query
				String queryString4 =
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
								"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
							    "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
							    "PREFIX seinfeld: <http://www.seinfeld.com/seinfeld/> " +
								"PREFIX ppl: <http://www.randompeople.com/ppl/> " +
								"PREFIX job: <http://www.job.com/job/> " +
								

								"SELECT ?Surname " +
								"WHERE {" +
								"      <seinfeld:Elaine> ppl:hasDated ?bn. " +
								"?bn ?pred ?dated." +
								" ?dated foaf:surname ?Surname .   }";

		        System.out.println("Issuing query #4....");

				//call this method to execute your SPARQL query
				issueSPARQL(queryString4);
				
				//Add your second query
				String queryString5 =
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
								"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
							    "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
							    "PREFIX seinfeld: <http://www.seinfeld.com/seinfeld/> " +
								"PREFIX ppl: <http://www.randompeople.com/ppl/> " +
								"PREFIX job: <http://www.job.com/job/> " +
								"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +

								"SELECT ?Person ?Job ?Salary ?Company ?BossName " +
								" WHERE {" +
								"?Person rdf:type <foaf:Person> . "+
								"?Person ppl:hasJob ?Job . "+
								" ?Job job:salary ?Salary . "+
								" OPTIONAL {?Job job:organization ?Company } "+ 
								" OPTIONAL {?Job job:boss ?BossName }"+ 
								" }";
								

		        System.out.println("Issuing query #5....");

				//call this method to execute your SPARQL query
				issueSPARQL(queryString5);
				
				//Add your second query
				String queryString6 =
						"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
								"PREFIX foaf: <http://xmlns.com/foaf/0.1/> " +
							    "PREFIX dc: <http://purl.org/dc/elements/1.1/> " +
							    "PREFIX seinfeld: <http://www.seinfeld.com/seinfeld/> " +
								"PREFIX ppl: <http://www.randompeople.com/ppl/> " +
								"PREFIX job: <http://www.job.com/job/> " +
								"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
								

          
        "SELECT ?person ?Father ?Mother ?Cousin " +
		" WHERE {" +
		"?person rdf:type <foaf:Person> . "+
		"OPTIONAL {?person ppl:hasFather ?Father } "+
		"OPTIONAL {?person ppl:hasMother ?Mother } "+
		"OPTIONAL {?person ppl:hasCousin ?Cousin } "+
		"FILTER (?person = <seinfeld:Jerry> || ?person = <seinfeld:George> ||  ?person = <seinfeld:Elaine>  )"+
		"} ";

		        System.out.println("Issuing query #6....");

				//call this method to execute your SPARQL query
				issueSPARQL(queryString6);


	}


    public static void issueSPARQL(String queryString) {

        Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet response = qe.execSelect();

		// Output query results
		ResultSetFormatter.out(System.out, response, query);

		// Important - free up resources used running the query
		qe.close();

    }//method issueSPARQL


}
