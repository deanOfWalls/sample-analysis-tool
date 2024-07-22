document.addEventListener("DOMContentLoaded", function() {
    const cameraPreview = document.getElementById("camera-preview");
    const scanMessage = document.getElementById("scan-message");
    let isScanningEnabled = true;

    function onScanSuccess(decodedText, decodedResult) {
        if (!isScanningEnabled) {
            return;
        }
        isScanningEnabled = false;

        fetch("/api/receive", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ sampleId: decodedText })
        })
        .then(response => response.text())
        .then(message => {
            if (message === "Sample not found or already in progress") {
                scanMessage.textContent = "Sample not found or already in progress";
                scanMessage.className = 'notification error';
            } else {
                scanMessage.textContent = "Scan Successful";
                scanMessage.className = 'notification success';
            }
            scanMessage.style.display = "block";
            setTimeout(() => {
                scanMessage.style.display = "none";
                isScanningEnabled = true;
            }, 2000); // Cooldown period
        })
        .catch(error => {
            console.error("Error:", error);
            scanMessage.textContent = "Error during scanning";
            scanMessage.className = 'notification error';
            scanMessage.style.display = "block";
            setTimeout(() => {
                scanMessage.style.display = "none";
                isScanningEnabled = true;
            }, 2000); // Cooldown period
        });
    }

    const html5QrCode = new Html5Qrcode("camera-preview");
    html5QrCode.start(
        { facingMode: "environment" },
        {
            fps: 10, // Optional, set the scanning speed
            qrbox: 250 // Optional, set the QR box size
        },
        onScanSuccess
    ).catch(err => {
        console.error("Unable to start scanning:", err);
    });
});
