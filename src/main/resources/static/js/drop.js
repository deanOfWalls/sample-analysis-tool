window.addEventListener('load', function () {
    const qrCodeSuccessCallback = (decodedText, decodedResult) => {
        console.log(`Code scanned = ${decodedText}`, decodedResult);
        fetch('/api/dropOff', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ sampleId: decodedText })
        })
        .then(response => response.json())
        .then(data => {
            const messageElement = document.getElementById('scan-message');
            messageElement.style.display = 'block';
            if (data.message === 'Sample already exists') {
                messageElement.textContent = 'Sample already scanned';
                messageElement.style.color = 'red';
            } else {
                messageElement.textContent = 'Scan Successful';
                messageElement.style.color = 'green';
            }
            setTimeout(() => {
                messageElement.style.display = 'none';
            }, 2000);
        })
        .catch(error => console.error('Error:', error));
    };

    const html5QrCode = new Html5Qrcode("camera-preview");
    html5QrCode.start(
        { facingMode: "environment" },
        {
            fps: 10,
            qrbox: { width: 250, height: 250 }
        },
        qrCodeSuccessCallback
    ).catch(err => console.error(`Error: ${err}`));
});
