# Introduction #

There are three projects involved in the development of new evaluators:
  * common/repositoryservice-tools
  * common/repositoryservice-api
  * analytics

This is a brief summary of the evaluation codebase as it currently stands.

# Analytics project #

This project contains virtually all of the evaluation code as well as code for generating the result HTML pages and their constituent tables, graphs, tar archives, etc. For evaluation, the base interface and abstract class are Evaluator and EvaluatorImpl. Task-specific evaluators extend these, e.g., Beat Evaluator. The actual calculation of evaluation metrics, e.g. "p-score", happen here.

In addition to the base evaluation code, each task has its own specific ResultRenderer, as each task calls for different types of plots/graphs and tables to be embedded in the HTML pages. Each task-specific ResultRenderer extends ResultRenderer/ResultRendererImpl.  The analytics project contains most of the codebase required for evaluating and generating evaluation reports for MIREX tasks. Each task has its own evaluation code and its own result renderer.

# RepositoryService-API project #

Defines the model and interfaces to the NEMA metadata repository.

Each task in MIREX has a specific file format that participating algorithms are asked to write out. In something like Key detection, the filetype is a single tab-delimited line in the form "A\tmajor." The filetype classes contain the read and write methods for these filetpes and populate a "NemaData" object with the salient information. The "NemaData" object is the main object used throughout the evaluation code (in the analytics package). Essentially, the contents of the file are read in and that data/metadata is put into a NemaData instance and passed to the evaluators.

(Note: The code in this package was moved from analytics a while back for a good but undocumented reason).

# RepositoryService-Tools project #

This project supports running evaluators via the command-line and not through the DIY service. It includes stand-alone evaluators that remove dependencies on the DIY infrastructure for running evaluation. The main dependency is that the metadata related to datasets used in evaluation and the metadata of the actual submitters are stored in NEMA-related databases. This project instead stores this information in properties files.  Note, that these still establish a database connection to the submission database, in order to get the metadata associated with each submitter and the corresponding URLs of their uploaded abstracts, etc.



(Note: Based on email from Andreas Ehmann to mirproject list on 7/12/2012 'NEMA-DIY code for MIREX Evaluators')