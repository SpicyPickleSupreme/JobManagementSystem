# Job Management System
## Simple Single Application Prototype

The goal of this system is to handle the execution of multiple types of Jobs.
## [Technical Paper](https://github.com/kingbishop/JobManagementSystem/blob/master/JMS.pdf)

### Main Components
- [Job](https://github.com/kingbishop/JobManagementSystem/blob/master/src/jms/system/Job.java)
- [JobExecutor](https://github.com/kingbishop/JobManagementSystem/blob/master/src/jms/system/JobExecutor.java)
- [JobScheduler](https://github.com/kingbishop/JobManagementSystem/blob/master/src/jms/system/JobScheduler.java)
- [JobManagementService](https://github.com/kingbishop/JobManagementSystem/blob/master/src/jms/system/JobManagementService.java)

### Implementation

Creating a command job that simply prints "Hello World"
```java
        /*Given a command and a priority*/
        Job printHelloWorld = new CommandJob(()->{
			System.out.println("Hello World");
			return JobCode.SUCCESS;
		},10);
```

Scheduling a Job
```java
    long scheduleFor = System.currentTimeMillis() + 1000 //1 second from start
    JobManagementService.scheduleJob(printHelloWorld, scheduleFor);
```

Starting the Job Management System, blocks
```java
    JobManagementService.start();
    /* 
        //Starting Async
        JobManagementService.startAsync();
    */
```