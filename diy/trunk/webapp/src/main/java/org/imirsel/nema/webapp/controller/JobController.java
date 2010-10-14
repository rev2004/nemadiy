package org.imirsel.nema.webapp.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.jcr.SimpleCredentials;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.imirsel.nema.flowservice.config.MeandreServerProxyStatus;
import org.imirsel.nema.meandre.util.ConsoleUtil;
import org.imirsel.nema.meandre.util.MeandreConsoleDao;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.Job.JobStatus;
import org.imirsel.nema.model.JobResult;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.Notification;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.User;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.jobs.DisplayResultSet;
import org.imirsel.nema.webapp.json.ConverterToList;
import org.imirsel.nema.webapp.json.ConverterToMap;
import org.imirsel.nema.webapp.json.ConverterToMapJob;
import org.imirsel.nema.webapp.json.ConverterToMapJobLong;
import org.imirsel.nema.webapp.json.ConverterToMapServer;
import org.imirsel.nema.webapp.json.ConverterToMapServerConfig;
import org.imirsel.nema.webapp.service.ResourceTypeService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;



/** 
 * 
 * @author kumaramit01
 * @since 0.4.0
 * note: comment some code about submission controllers for changing API in upstream at 0.7.0. 
 */
public class JobController extends MultiActionController {

    static private Log logger = LogFactory.getLog(JobController.class);
    private UserManager userManager = null;
    private FlowService flowService = null;
    private RepositoryClientConnectionPool repositoryClientConnectionPool;
    private MeandreConsoleDao meandreConsoleDao;
    private ConsoleUtil consoleUtil;
    private ResourceTypeService resourceTypeService;

    public void setConsoleUtil(ConsoleUtil consoleUtil) {
        this.consoleUtil = consoleUtil;
    }

    public void setMeandreConsoleDao(MeandreConsoleDao meandreConsoleDao) {
        this.meandreConsoleDao = meandreConsoleDao;
    }

    public FlowService getFlowService() {
        return flowService;
    }

    public void setFlowService(FlowService flowService) {
        this.flowService = flowService;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * @return the repositoryClientConnectionPool
     */
    public RepositoryClientConnectionPool getRepositoryClientConnectionPool() {
        return repositoryClientConnectionPool;
    }

    /**
     * @param repositoryClientConnectionPool
     *            the repositoryClientConnectionPool to set
     */
    public void setRepositoryClientConnectionPool(
            RepositoryClientConnectionPool repositoryClientConnectionPool) {
        this.repositoryClientConnectionPool = repositoryClientConnectionPool;
    }

    /**
     * @return the userManager
     */
    public UserManager getUserManager() {
        return userManager;
    }

 
    /**
     * Returns the job detail of the job with id
     *
     * @param request
     * @param response
     * @return
     */
    public ModelAndView getJobDetail(HttpServletRequest req,
            HttpServletResponse res) throws IOException {
        String hostName = "http://" + req.getServerName() + ":"
                + req.getServerPort();
        String _jobId = req.getParameter("id");
        long jobId = Long.parseLong(_jobId);
        Job job = flowService.getJob(jobId);
        for (JobResult result : job.getResults()) {
            result.setUrl(processUrl(result.getUrl(), hostName));
        }

        logger.debug("start to render displayed results");
        DisplayResultSet resultSet = new DisplayResultSet(job.getResults());

        ModelAndView mav = null;
        String uri = req.getRequestURI();
        if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
            mav = new ModelAndView("jsonView");
            ConverterToMap<Job> converter = new ConverterToMapJobLong();
            mav.addObject(Constants.JOB, converter.convertToMap(job));
            mav.addObject("resultSet", resultSet);

        } else {
            mav = new ModelAndView("job/job");
            mav.addObject(Constants.JOB, job);
            mav.addObject("resultSet", resultSet);

            MeandreServerProxyConfig head = flowService.getHeadConfig();
            mav.addObject("head", head);
        }

        return mav;

    }



    /**
     * Allows users to delete or abort a job
     *
     * @param req
     * @param res
     * @return
     * @throws MeandreServerException
     */
    public ModelAndView doJobAction(HttpServletRequest req,
            HttpServletResponse res) throws MeandreServerException {
        String _jobId = req.getParameter("id");
        long jobId = Long.parseLong(_jobId);

        Job job = flowService.getJob(jobId);
        String _submitType = req.getParameter("submit");

        if (_submitType.equals("Abort This Job")) {
            flowService.abortJob(job.getId());
        } else if (_submitType.equals("Delete This Job")) {
            flowService.deleteJob(job.getId());
            logger.info("deleting flow: " + job.getFlow().getUri());
            flowService.removeFlow(job.getFlow().getUri());
            if (job.isDone()) {
                consoleUtil.deleteConsole(job);
            }
        }
        // , Constants.JOB, job
        return new ModelAndView(
                new RedirectView("JobManager.getUserJobs", true));
    }

    /**
     * Returns the list of jobs for the current user
     *
     * @param request
     * @param response
     * @return
     */
    public ModelAndView getUserJobs(HttpServletRequest req,
            HttpServletResponse res) throws IOException {
        User user = userManager.getCurrentUser();
        logger.debug("getting user " + user.getUsername());
        long userId = user.getId();
        logger.debug("start to list the jobs of   " + user.getUsername());

        String keyword = req.getParameter("keyword");
        boolean isTaskIdFiltered = false;
        Long taskId = null;
        try {
            taskId = Long.valueOf(req.getParameter("taskId"));
            if (taskId != -100L) {
                isTaskIdFiltered = true;
            }
        } catch (NumberFormatException e) {
        }
        List<Job> allJobs;
        if (isTaskIdFiltered) {
            allJobs = flowService.getUserJobsByTaskId(userId, taskId);
        } else if ((keyword != null) && (keyword.length() > 0)) {
            allJobs = flowService.getUserJobsByKeyword(userId, keyword);
        } else {
            allJobs = flowService.getUserJobs(userId);
        }

        String type = req.getParameter("type");
        Set<JobStatus> filterSet = new HashSet<JobStatus>();
        if ("running".equalsIgnoreCase(type)) {
            filterSet.add(JobStatus.SCHEDULED);
            filterSet.add(JobStatus.SUBMITTED);
            filterSet.add(JobStatus.STARTED);
        } else if ("aborted".equalsIgnoreCase(type)) {
            filterSet.add(JobStatus.ABORTED);
            filterSet.add(JobStatus.FAILED);

        } else if ("finished".equalsIgnoreCase(type)) {
            filterSet.add(JobStatus.FINISHED);
        } else {
            filterSet.addAll((Arrays.asList(JobStatus.values())));
        }
        List<Job> jobs = new ArrayList<Job>();
        for (Job job : allJobs) {
            if (filterSet.contains(job.getJobStatus())) {
                jobs.add(job);
            }
        }

        ModelAndView mav = null;
        String uri = (req != null) ? req.getRequestURI() : "";
        if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {

            ConverterToList<Job> converter = new ConverterToList<Job>();
            mav = new ModelAndView("jsonView", Constants.JOBLIST,
                    converter.convertToList(jobs, new ConverterToMapJob()));
            // mav =new ModelAndView("jsonView",Constants.JOBLIST,jobs);
        } else {
            mav = new ModelAndView("job/jobList", Constants.JOBLIST, jobs);
        }
        try {
            List<NemaTask> tasks = resourceTypeService.getSupportedTasks();
            mav.addObject("taskIds", tasks);
        } catch (SQLException e) {
            logger.error(e, e);
        }
        return mav;
    }

    /**
     * Retrieve the running time for all finished jobs.
     * @param req
     * @param res
     * @return
     * @throws IOException
     */
    public ModelAndView getRunningTime(HttpServletRequest req,
            HttpServletResponse res) throws IOException {

        User user = userManager.getCurrentUser();
        logger.debug("getting user " + user.getUsername());
        long userId = user.getId();
        logger.debug("start to list the jobs of   " + user.getUsername());
        List<Job> allJobs = flowService.getUserJobs(userId);

        Set<JobStatus> filterSet = new HashSet<JobStatus>();
        filterSet.add(JobStatus.FINISHED);
        filterSet.add(JobStatus.ABORTED);

        Collections.sort(allJobs,
                new Comparator<Job>() {

                    @Override
                    public int compare(Job o1, Job o2) {
                        Date o1Time = (o1.getScheduleTimestamp() == null ? new Date() : o1.getScheduleTimestamp());
                        Date o2Time = (o2.getScheduleTimestamp() == null ? new Date() : o2.getScheduleTimestamp());
                        return o1Time.compareTo(o2Time);
                    }
                });

        String type = req.getParameter("type");
        // Set<JobStatus> filterSet=new HashSet<JobStatus>();
        List<NemaTask> tasks = null;
        try {
            tasks = resourceTypeService.getSupportedTasks();
        } catch (SQLException e) {
            logger.error(e, e);
        }
        Map<NemaTask, Map<Flow, Map<String, Job>>> taskJobMap = new HashMap<NemaTask, Map<Flow, Map<String, Job>>>();
        Map<Integer, NemaTask> taskMap = new HashMap<Integer, NemaTask>();
        for (NemaTask task : tasks) {
            taskJobMap.put(task, new HashMap<Flow, Map<String, Job>>());
            taskMap.put(task.getId(), task);
        }
        List<Job> jobs = new ArrayList<Job>();
        Map<Job, String> duration = new HashMap<Job, String>();
        for (Job job : allJobs) {
            if ((filterSet.contains(job.getJobStatus()))
                    && (!"NON-SUBMISSION".equalsIgnoreCase(job.getFlow().getSubmissionCode()))
                    && (job.getEndTimestamp() != null)) {

                SimpleCredentials credential = userManager.getCurrentUserCredentials();
                Map<Component, List<Property>> componentMap = flowService.getAllComponentsAndPropertyDataTypes(credential, job.getFlow().getUri());
                Property taskId = searchProperty(componentMap, "taskID");
                if (taskId != null) {
                    try {
                        Map<Flow, Map<String, Job>> flowJobMap = taskJobMap.get(taskMap.get(Integer.parseInt(taskId.getValue())));
                        Flow template = findTemplate(job.getFlow());
                        if (!flowJobMap.containsKey(template)) {
                            flowJobMap.put(template, new TreeMap<String, Job>());
                        }
                        flowJobMap.get(template).put(job.getFlow().getSubmissionCode(), job);
                        Long interval = (job.getEndTimestamp().getTime() - job.getSubmitTimestamp().getTime()) / 1000;
                        long hr = interval / 3600;
                        interval -= hr * 3600;
                        long min = (interval / 60);
                        interval -= min * 60;
                        duration.put(job, String.format("%02d:%02d:%02d", hr,
                                min, interval));
                        logger.debug("save run time of job " + job.getId()
                                + " (" + job.getName());
                    } catch (NumberFormatException e) {
                        logger.error(e, e);
                    }
                }
            }
        }

        for (NemaTask task : tasks) {
            if (taskJobMap.get(task).isEmpty()) {
                taskJobMap.remove(task);
            }
        }

        saveRuntimeOnFiles(taskJobMap, duration);

        ModelAndView mav = new ModelAndView("job/runTime");
        mav.addObject("taskJobMap", taskJobMap);
        mav.addObject("duration", duration);

        return mav;
    }
    final static String STOREPATH = "runtime";

    private void saveRuntimeOnFiles(Map<NemaTask, Map<Flow, Map<String, Job>>> taskJobMap,
            Map<Job, String> duration) {
        try {
            BufferedWriter wiki = new BufferedWriter(new FileWriter(STOREPATH
                    + File.separator + "wiki.txt"));

            for (NemaTask task : taskJobMap.keySet()) {
                for (Flow flow : taskJobMap.get(task).keySet()) {
                    wiki.append("'''Task ").append(String.valueOf(task.getId())).append("'''").append(" (").append(task.getName()).append(")");
                    wiki.newLine();
                    wiki.append(":").append(task.getDescription());
                    wiki.newLine();
                    wiki.append(":'''Flow ").append(String.valueOf(flow.getId())).append("''' -").append(flow.getName());
                    wiki.newLine();
                    wiki.append("<csv>").append("2010/runtime/task").append(String.valueOf(task.getId())).append("flow").append(String.valueOf(flow.getId())).append(".csv").append("</csv>");
                    wiki.newLine();
                    wiki.newLine();

                    BufferedWriter taskCsv = new BufferedWriter(new FileWriter(STOREPATH
                            + File.separator + "task" + task.getId() + "flow" + flow.getId() + ".csv"));
                    taskCsv.append("*Submission Code,Runtime");
                    taskCsv.newLine();
                    for (Job job : taskJobMap.get(task).get(flow).values()) {
                        taskCsv.append(job.getFlow().getSubmissionCode()).append(',').append(duration.get(job));
                        taskCsv.newLine();
                    }
                    taskCsv.close();
                }
            }
            wiki.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Flow findTemplate(Flow flow) {
        if (flow.isTemplate() || (flow.getInstanceOf() == null) || (flow.getInstanceOf() == flow)) {
            return flow;
        } else {
            return findTemplate(flow.getInstanceOf());
        }
    }

    private Property searchProperty(
            Map<Component, List<Property>> componentMap, String name) {
        for (List<Property> list : componentMap.values()) {
            Property prop = findProperty(list, name);
            if (prop != null) {
                return prop;
            }
        }
        return null;
    }

    private Property findProperty(Collection<Property> properties, String name) {
        if (name != null) {
            for (Property property : properties) {
                if (name.equals(property.getName())) {
                    return property;
                }
            }
        }
        return null;
    }

    /**
     * Returns list of notifications for the user
     *
     * @param req
     * @param res
     * @return
     */
    public ModelAndView getNotification(HttpServletRequest req,
            HttpServletResponse res) {
        User user = this.userManager.getCurrentUser();
        List<Notification> notifications = this.flowService.getUserNotifications(user.getId());
        ModelAndView mav = new ModelAndView("jsonView");

        if (System.currentTimeMillis() % 2 == 0) {
            mav.addObject("notifications", notifications);
        } else {
            mav.addObject("notifications", null);
        }

        return mav;
    }

    /**
     * Returns the console
     *
     * @param request
     *            requires jobId http request parameter
     * @param response
     * @return console The String output
     * @throws TransmissionException
     */
    public ModelAndView getConsole(HttpServletRequest request,
            HttpServletResponse response) throws MeandreServerException {
        String _jobId = request.getParameter("jobId");
        long jobId = Long.parseLong(_jobId);
        Job job = this.flowService.getJob(jobId);
        response.setContentType("text/plain");

        try {
            if (job.isDone()) {
//                if (job.getConsoleStatus() == Job.ConsoleStatus.NOTDUMPED) {
//                    TODO
//                    dump the file, however, problems might arise
//                    when the new request come before the last dumping finish.
//                }
                response.getOutputStream().print(consoleUtil.findConsole(job));
            } else {
                String text = this.flowService.getConsole(job);
                response.getOutputStream().println(text);
            }
        } catch (IOException e) {
            logger.error(e, e);
        }

        return null;
    }

    /**
     * Query for the status of servers
     */
    public ModelAndView getServerStatus(HttpServletRequest req,
            HttpServletResponse res) throws IOException {
        Map<MeandreServerProxyConfig, MeandreServerProxyStatus> workers = flowService.getWorkerStatus();
        MeandreServerProxyConfig head = flowService.getHeadConfig();
        List<Job> scheduledJobs = flowService.getScheduledJobs();

        ModelAndView mav;
        String uri = (req != null) ? req.getRequestURI() : "";
        if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {

            mav = new ModelAndView("jsonView");

            ConverterToList<Job> converter1 = new ConverterToList<Job>();
            mav.addObject("scheduledJobs", converter1.convertToList(
                    scheduledJobs, new ConverterToMapJob()));

            ConverterToList<Entry<MeandreServerProxyConfig, MeandreServerProxyStatus>> converter2 = new ConverterToList<Entry<MeandreServerProxyConfig, MeandreServerProxyStatus>>();
            mav.addObject("workers", converter2.convertToList(
                    workers.entrySet(), new ConverterToMapServer()));
            mav.addObject("anotherWorkers", workers);

            ConverterToMapServerConfig converter3 = new ConverterToMapServerConfig();
            mav.addObject("head", converter3.convertToMap(head));

        } else {
            mav = new ModelAndView("job/serverStatus");
            mav.addObject("workers", workers.entrySet());
            mav.addObject("head", head);
            mav.addObject("scheduledJobs", scheduledJobs);
        }

        return mav;
    }

    /**
     * return the status of nema for the status bar, only Json interface.
     *
     * @param req
     * @param res
     * @return
     */
    public ModelAndView getNemaStatus(HttpServletRequest req,
            HttpServletResponse res) {
        ModelAndView mav = new ModelAndView("jsonView");

        Map<MeandreServerProxyConfig, MeandreServerProxyStatus> workers = flowService.getWorkerStatus();

        List<Job> scheduledJobs = flowService.getScheduledJobs();
        int availableSlots = 0, runningNum = 0;
        for (Entry<MeandreServerProxyConfig, MeandreServerProxyStatus> entry : workers.entrySet()) {
            availableSlots += entry.getKey().getMaxConcurrentJobs();
            runningNum += entry.getValue().getNumRunning();
        }
        mav.addObject("load", runningNum * 1.0 / availableSlots);
        mav.addObject("jobsInQueue", scheduledJobs.size());
        return mav;
    }

    public ModelAndView dumpConsole(HttpServletRequest req,
            HttpServletResponse rep) {

        Map<Job, String> dumpResult = new HashMap<Job, String>();
        List<Job> jobs = flowService.getUserJobs(userManager.getCurrentUser().getId());
        for (Job job : jobs) {
            if (job.isDone()) {
                try {
                    consoleUtil.dumpConsoleToFile(job);
                    dumpResult.put(job, "successfully dumped");
                } catch (IllegalArgumentException e) {
                    dumpResult.put(job, e.getMessage());
                } catch (Exception e) {
                    dumpResult.put(job, "error in dumping");
                }
            } else {
                dumpResult.put(job, "not finished yet, no dump");
            }
        }

        ModelAndView mav = new ModelAndView("job/dumpResult", "dumpResult",
                dumpResult);
        return mav;
    }

    private String processUrl(String url, String hostName) {
        String identifier = "published_resources/nema";
        int index = url.indexOf(identifier);
        String resultFolder = url.substring(index + identifier.length());
        return hostName + "/nema_out" + resultFolder;

    }

    private RepositoryClientInterface getRepositoryClient() throws SQLException {
        return repositoryClientConnectionPool.getFromPool();
    }

    public void setResourceTypeService(ResourceTypeService resourceTypeService) {
        this.resourceTypeService = resourceTypeService;
    }
}
