+---------------------------------------------------------------+
|                   Sample Analysis Workflow                    |
+---------------------------------------------------------------+

[1] FileMaker App
 - User Action: Create sampleNumber, print label with QR code
 - QR Code contains: sampleNumber, date

    |
    v

[2] Sample Drop-off Kiosk
 - User Action: Scan QR code at kiosk
 - System Action: Call SampleController (/drop endpoint)
   +---------------------------------------------+
   | SampleController                            |
   | - Method: showDropPage()                    |
   | - Action: Display drop.html for scanning    |
   +---------------------------------------------+
   | - Method: dropOffSample()                   |
   | - Action: Adds sample to queue              |
   |   Calls SampleService.addSample()           |
   +---------------------------------------------+
   | SampleService                               |
   | - Method: addSample(sampleId, "Awaiting")   |
   | - Action: Create Sample object              |
   |   Save to repository                        |
   |   Calls SampleRepository.save()             |
   +---------------------------------------------+
   | SampleRepository                            |
   | - Action: Save Sample entity to database    |
   +---------------------------------------------+

    |
    v

+-------------------------------+
| Awaiting Analysis Queue       |
| - Status: "Awaiting Analysis" |
+-------------------------------+

    |
    v

[3] Lab Kiosk (Receiving)
 - User Action: Scan QR code at lab kiosk
 - System Action: Call SampleController (/receive endpoint)
   +-------------------------------------------------------+
   | SampleController                                      |
   | - Method: showReceivePage()                           |
   | - Action: Display receive.html for scanning           |
   +-------------------------------------------------------+
   | - Method: receiveSample()                             |
   | - Action: Update sample status                        |
   |   Calls SampleService.updateSampleStatus()            |
   +-------------------------------------------------------+
   | SampleService                                         |
   | - Method: updateSampleStatus(sampleId, "In Progress") |
   | - Action: Update Sample object                        |
   |   Set status to "In Progress"                         |
   |   Calls SampleRepository.save()                       |
   +-------------------------------------------------------+
   | SampleRepository                                      |
   | - Action: Update Sample entity in database            |
   +-------------------------------------------------------+

    |
    v

+------------------------------+
| In Progress Queue            |
| - Status: "In Progress"      |
+------------------------------+

    |
    v

[4] Lab Analysis
 - User Action: Lab analyst analyzes sample
 - (Future) Integration: Call company API to retrieve data
   based on sampleNumber during analysis

    |
    v

[5] Lab Kiosk (Completion)
 - User Action: Scan QR code at lab kiosk
 - System Action: Call SampleController (/complete endpoint)
   +-----------------------------------------------------+
   | SampleController                                    |
   | - Method: showCompletePage()                        |
   | - Action: Display complete.html for scanning        |
   +-----------------------------------------------------+
   | - Method: completeSample()                          |
   | - Action: Update sample status                      |
   |   Calls SampleService.updateSampleStatus()          |
   +-----------------------------------------------------+
   | SampleService                                       |
   | - Method: updateSampleStatus(sampleId, "Completed") |
   | - Action: Update Sample object                      |
   |   Set status to "Completed"                         |
   |   Calls SampleRepository.save()                     |
   +-----------------------------------------------------+
   | SampleRepository                                    |
   | - Action: Update Sample entity in database          |
   +-----------------------------------------------------+

    |
    v

+------------------------------+
| Completed Queue              |
| - Status: "Completed"        |
+------------------------------+

+---------------------------------------------------------------+
|                   Supporting Components                       |
+---------------------------------------------------------------+

[Database]
 - Stores all Sample entities
 - Attributes: sampleId, status, scannedInTime, receivedTime, completedTime

[Thymeleaf Templates]
 - drop.html, receive.html, complete.html, queue.html
 - User interfaces for different operations

[JavaScript]
 - Scanning logic and interaction with backend endpoints

+---------------------------------------------------------------+
|                    Additional Notes                           |
| 1. Production workers create sampleNumber in FileMaker.       |
| 2. Print label with sampleNumber, date, and QR code.          |
| 3. Scan QR code at drop-off kiosk.                            |
| 4. Sample added to "Awaiting Analysis" queue.                 |
| 5. Analyst scans sample at lab kiosk to mark "In Progress".   |
| 6. Scan again to mark as "Completed" and remove from queue.   |
| 7. Future enhancements:                                       |
|    - Mark sample as urgent                                    |
|    - Retrieve data from company API during queue population   |
+---------------------------------------------------------------+
