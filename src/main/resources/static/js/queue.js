var pollInterval = 5000; // Poll every 5 seconds
var countdown = pollInterval / 1000; // Countdown in seconds

function fetchQueueData() {
    fetch('/api/queue')
        .then(response => response.json())
        .then(samples => {
            updateQueue(samples);
            resetCountdown();
        })
        .catch(error => console.error('Error fetching queue data:', error));
}

function updateQueue(samples) {
    var tableBody = document.querySelector("table tbody");
    tableBody.innerHTML = ""; // Clear existing rows
    samples.forEach(sample => {
        var statusColor = getStatusColor(sample.status);
        var row = document.createElement("tr");
        row.innerHTML = `
            <td>${sample.sampleId}</td>
            <td style="color:${statusColor}; font-style:italic;">${sample.status}</td>
            <td>${sample.scannedInTime.substring(11, 16)} ${sample.scannedInTime.substring(0, 10)}</td>
            <td>${sample.receivedTime ? sample.receivedTime.substring(11, 16) + ' ' + sample.receivedTime.substring(0, 10) : ''}</td>
            <td>${sample.completedTime ? sample.completedTime.substring(11, 16) + ' ' + sample.completedTime.substring(0, 10) : ''}</td>
            <td>${sample.elapsedTime}</td>
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

function startCountdown() {
    var timerElement = document.getElementById('countdown-timer');
    countdown = pollInterval / 1000; // Reset countdown
    var countdownInterval = setInterval(() => {
        countdown--;
        timerElement.textContent = `Update in ${countdown} seconds`;
        if (countdown <= 0) {
            clearInterval(countdownInterval);
            timerElement.textContent = `Updating...`;
        }
    }, 1000);
}

function resetCountdown() {
    startCountdown();
}

window.addEventListener('load', function() {
    fetchQueueData();
    setInterval(fetchQueueData, pollInterval); // Fetch data every 5 seconds
    startCountdown();
});
