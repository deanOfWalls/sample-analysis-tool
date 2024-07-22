var pollInterval = 5000; // Poll every 5 seconds
var countdown = pollInterval / 1000; // Countdown in seconds
var timerElement;
var countdownInterval;

function fetchQueueData() {
    fetch('/api/queue')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(samples => {
            if (!Array.isArray(samples)) {
                throw new TypeError('Expected an array');
            }
            updateQueue(samples);
            setTimeout(resetCountdown, 1000); // Show "Updating..." for 1 second
        })
        .catch(error => console.error('Error fetching queue data:', error));
}

function updateQueue(samples) {
    var tableBody = document.querySelector("table tbody");
    tableBody.innerHTML = ""; // Clear existing rows
    samples.forEach(sample => {
        var statusColor = getStatusColor(sample.status);
        var elapsedTime = calculateElapsedTime(sample.scannedInTime);
        var row = document.createElement("tr");
        row.innerHTML = `
            <td>${sample.sampleId}</td>
            <td style="color:${statusColor}; font-style:italic;">${sample.status}</td>
            <td>${sample.scannedInTime ? sample.scannedInTime.substring(11, 16) + ' ' + sample.scannedInTime.substring(0, 10) : ''}</td>
            <td>${sample.receivedTime ? sample.receivedTime.substring(11, 16) + ' ' + sample.receivedTime.substring(0, 10) : ''}</td>
            <td>${elapsedTime}</td>
        `;
        tableBody.appendChild(row);
    });
}

function getStatusColor(status) {
    switch (status) {
        case 'Awaiting Analysis':
            return 'orange';
        case 'Analysis In Progress':
            return 'green';
        case 'Completed':
            return 'blue';
        default:
            return 'black';
    }
}

function calculateElapsedTime(scannedInTime) {
    var scannedDate = new Date(scannedInTime);
    var now = new Date();
    var elapsedMilliseconds = now - scannedDate;
    var elapsedSeconds = Math.floor(elapsedMilliseconds / 1000);
    var hours = Math.floor(elapsedSeconds / 3600);
    var minutes = Math.floor((elapsedSeconds % 3600) / 60);
    var seconds = elapsedSeconds % 60;
    return `${hours}h ${minutes}m ${seconds}s`;
}

function startCountdown() {
    countdown = pollInterval / 1000; // Reset countdown
    countdownInterval = setInterval(() => {
        if (countdown <= 0) {
            clearInterval(countdownInterval);
            timerElement.textContent = `Updating...`;
            fetchQueueData();
        } else {
            timerElement.textContent = `Update in ${countdown} seconds`;
            countdown--;
        }
    }, 1000);
}

function resetCountdown() {
    if (timerElement) {
        clearInterval(countdownInterval);
        timerElement.textContent = `Update in ${pollInterval / 1000} seconds`;
    }
    startCountdown();
}

window.addEventListener('load', function() {
    timerElement = document.getElementById('countdown-timer');
    fetchQueueData();
    startCountdown();
});
