# Sample Analysis Tool

## Overview

The Sample Analysis Tool is a Spring Boot application designed to streamline the lab sample analysis process and enhance accountability. The system allows production workers to efficiently manage and track samples from creation to completion using QR codes.

## Features

1. **Sample Creation**:
   - Production workers create a `sampleNumber` in FileMaker.
   - A label containing the `sampleNumber`, date, and a QR code encoded with the `sampleNumber` is printed.

2. **Sample Drop-off**:
   - Samples are scanned into a kiosk at a sample drop-off site.
   - (Future Feature) The option to flag a sample as urgent before scanning may be added.

3. **Queue Management**:
   - Upon scanning, the sample is added to a queue.
   - (Upcoming Feature) The system will call a company API endpoint to retrieve data associated with the `sampleNumber` encoded in the QR code, populating the queue with relevant information.

4. **Sample Processing**:
   - When a lab analyst retrieves the sample from the drop-off, they scan it at a lab kiosk, updating the status from "Awaiting Analysis" to "In-Progress".
   - Upon completing the analysis, the analyst scans the sample once more at the lab kiosk, marking it as "Completed" and clearing it from the queue.

## Technologies Used

- **Backend**: Java, Spring Boot, Spring Data JPA, H2 Database
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript
- **QR Code Processing**: ZXing library

### Prerequisites

- Java 17
- Maven
- Any web browser


