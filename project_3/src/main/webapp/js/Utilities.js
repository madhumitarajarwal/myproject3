
function handleIntegerInput(inputElement, errorElementId, maxLength) {
    const errorMessage = document.getElementById(errorElementId);
    const currentValue = inputElement.value;

    if (isNaN(currentValue) || /[^0-9]/.test(currentValue)) {
        errorMessage.textContent = 'Only numbers are allowed.';
        inputElement.value = currentValue.replace(/\D/g, '');
    } else {
        if (currentValue.length > maxLength) {
            errorMessage.textContent = `Only ${maxLength} digits are allowed.`;
            inputElement.value = currentValue.slice(0, maxLength);
        } else {
            errorMessage.textContent = '';
        }
    }
    inputElement.value = inputElement.value.replace(/^0+(?=\d)/, '').slice(0, maxLength);
}


function processNumber(num) {
    if (typeof num === 'number' && !isNaN(num)) {
        return `${num} is a number.`;
    } else {
        throw new TypeError("Only numbers (int and float) are allowed.");
    }
}


function validateAlphanumericInput(inputElement, errorElementId, maxLength) {
    const errorMessage = document.getElementById(errorElementId);
    const currentValue = inputElement.value;

    // Check for maximum length
    if (currentValue.length > maxLength) {
        errorMessage.textContent = 'Input must not exceed ' + maxLength + ' characters.';
    } 
    // Check for alphanumeric characters only
    else if (!/^[a-zA-Z0-9]*$/.test(currentValue)) {
        errorMessage.textContent = 'Input must be alphanumeric only.';
    } 
    // Clear error message if input is valid
    else {
        errorMessage.textContent = '';
    }
}



function validateIntegerInput(inputElement, errorElementId, maxLength) {
    const errorMessage = document.getElementById(errorElementId);
    const currentValue = inputElement.value;

    if (!isNaN(currentValue) && currentValue.length <= maxLength) {
        errorMessage.textContent = '';
    }
}

function handleLetterInput(inputElement, errorElementId, maxLength) {
    const errorMessage = document.getElementById(errorElementId);
    let currentValue = inputElement.value;
    // Allow letters, spaces, and dots, but not at the beginning
    const allowedPattern = /^[a-zA-Z][a-zA-Z\s.]*$/;
    // Check if the first character is a letter
    if (currentValue.length > 0 && !/^[a-zA-Z]/.test(currentValue[0])) {
        errorMessage.textContent = 'Only letter are allowed';
        currentValue = currentValue.replace(/^[^a-zA-Z]+/, '');
    } else if (!allowedPattern.test(currentValue)) {
        errorMessage.textContent = 'Only letters are allowed.';
        currentValue = currentValue.replace(/[^a-zA-Z\s.]/g, '');
    } else {
        if (currentValue.length > maxLength) {
            errorMessage.textContent = `Only ${maxLength} characters are allowed.`;
            currentValue = currentValue.slice(0, maxLength);
        } else {
            errorMessage.textContent = '';
        }
    }

    inputElement.value = currentValue.slice(0, maxLength);
}
 
function validateLetterInput(inputElement, errorElementId, maxLength) {
    const errorMessage = document.getElementById(errorElementId);
    const currentValue = inputElement.value;

    if (/^[a-zA-Z]+$/.test(currentValue) && currentValue.length <= maxLength) {
        errorMessage.textContent = '';
    }
}

function initializeDatePicker(datePickerId, errorElementId, dateFormat = 'mm/dd/yy') {
    if (!$(`#${datePickerId}`).length) {
        return;
    }

    $(function() {
        $(`#${datePickerId}`).datepicker({
            dateFormat: dateFormat,
            onSelect: function() {
                clearErrorMessage(errorElementId);
            }
        });
    });
}

function setupDropdownListener(dropdownName, errorElementId) {
    const observer = new MutationObserver(function(mutations) {
        mutations.forEach(function(mutation) {
            if (mutation.type === 'childList') {
                const dropdowns = document.getElementsByTagName("select");

                for (let i = 0; i < dropdowns.length; i++) {
                    if (dropdowns[i].name === dropdownName && !dropdowns[i].dataset.listenerAdded) {
                        dropdowns[i].dataset.listenerAdded = 'true';
                        dropdowns[i].addEventListener("change", function() {
                            clearErrorMessage(errorElementId);
                        });
                    }
                }
            }
        });
    });

    observer.observe(document.body, { childList: true, subtree: true });
}

function clearErrorMessage(errorElementId) {
    const errorElement = document.getElementById(errorElementId);
    if (errorElement) {
        errorElement.innerText = '';
    }
}

function handleMobileNumberInput(inputElement, errorElementId, maxLength) {
    const errorMessage = document.getElementById(errorElementId);
    const currentValue = inputElement.value.trim();

    if (isNaN(currentValue) || /[^0-9]/.test(currentValue)) {
        errorMessage.textContent = 'Please enter only numbers.';
        inputElement.value = currentValue.replace(/\D/g, '');
    } else {
        if (currentValue.length > maxLength) {
            errorMessage.textContent = `Maximum ${maxLength} digits allowed.`;
            inputElement.value = currentValue.slice(0, maxLength);
        } else {
            if (currentValue.length > 0 && currentValue.charAt(0) <= '5') {
                errorMessage.textContent = 'Mobile number must start with a digit greater than 5.';
                inputElement.value = '';
            } else {
                errorMessage.textContent = '';
            }
        }
    }
}




function handleDoubleInput(inputElement, errorElementId, maxLength) {
    const errorMessage = document.getElementById(errorElementId);
    let currentValue = inputElement.value;

    // Clear the error message initially
    errorMessage.textContent = '';

    // Allow only numbers and a single decimal point
    const validValue = currentValue.replace(/[^0-9.]/g, '');
    if (validValue !== currentValue) {
        errorMessage.textContent = 'Only numbers and a single decimal point are allowed.';
        currentValue = validValue;
    }

    // Count decimal points
    const decimalCount = (currentValue.match(/\./g) || []).length;
    if (decimalCount > 1) {
        errorMessage.textContent = 'Only one decimal point is allowed.';
        const parts = currentValue.split('.');
        currentValue = parts[0] + '.' + parts.slice(1).join('');
    }

    // Handle leading zeros (allow "0." but not unnecessary leading zeros)
    if (currentValue.startsWith('0') && !currentValue.startsWith('0.') && currentValue.length > 1) {
        currentValue = currentValue.replace(/^0+/, '') || '0';
    }

    // Split into integer and decimal parts
    const parts = currentValue.split('.');
    let integerPart = parts[0];
    let decimalPart = parts[1] || '';

    // Allow typing a decimal point after a number (e.g., "123.")
    if (currentValue.endsWith('.') && decimalCount === 1) {
        inputElement.value = currentValue;
        return;
    }

    // Limit decimal places to 2
    if (decimalPart.length > 2) {
        errorMessage.textContent = 'Only 2 decimal places are allowed.';
        decimalPart = decimalPart.slice(0, 2);
    }

    // Enforce maximum length constraint (integer + decimal combined)
    if (integerPart.length + decimalPart.length > maxLength) {
        errorMessage.textContent = `Input exceeds the maximum length of ${maxLength} digits.`;
        const allowedDecimalLength = Math.max(0, maxLength - integerPart.length);
        decimalPart = decimalPart.slice(0, allowedDecimalLength);
        integerPart = integerPart.slice(0, maxLength - decimalPart.length);
    }

    // Update the input element's value
    if (decimalPart) {
        inputElement.value = `${integerPart}.${decimalPart}`;
    } else {
        inputElement.value = integerPart;
    }
}






/*function validateDoubleInput(inputElement, errorElementId, maxLength) {
    const errorMessage = document.getElementById(errorElementId);
    const currentValue = inputElement.value;

    errorMessage.textContent = '';

    if (isNaN(currentValue) || /[^0-9.]/.test(currentValue)) {
        errorMessage.textContent = 'Only numbers and a single decimal point are allowed.';
        return;
    }

    if ((currentValue.match(/\./g) || []).length > 1) {
        errorMessage.textContent = 'Only one decimal point is allowed.';
        return;
    }

    const parts = currentValue.split('.');
    const integerPart = parts[0];
    const decimalPart = parts[1] || '';

    if (integerPart.length + decimalPart.length > maxLength) {
        errorMessage.textContent = `Input exceeds the maximum length of ${maxLength} digits.`;
        return;
    }

    errorMessage.textContent = '';
}*/


function validateDoubleInput(inputElement, errorElementId, maxLength) {
    const errorMessage = document.getElementById(errorElementId);
    let currentValue = inputElement.value;

    // Clear previous error messages
    errorMessage.textContent = '';

    // Remove invalid characters (keep digits and a single decimal point)
    currentValue = currentValue.replace(/[^0-9.]/g, '');
    
    // Check for multiple decimal points
    const decimalCount = (currentValue.match(/\./g) || []).length;
    if (decimalCount > 1) {
        errorMessage.textContent = 'Only one decimal point is allowed.';
        const parts = currentValue.split('.');
        currentValue = parts[0] + '.' + parts.slice(1).join('');
    }

    // Handle leading zeros
    if (currentValue.startsWith('0') && !currentValue.startsWith('0.')) {
        currentValue = currentValue.replace(/^0+/, '') || '0';
    }

    // Split into integer and decimal parts
    const parts = currentValue.split('.');
    let integerPart = parts[0];
    let decimalPart = parts[1] || '';

    // Check if input exceeds maximum length
    if (integerPart.length + decimalPart.length > maxLength) {
        errorMessage.textContent = `Input exceeds the maximum length of ${maxLength} digits.`;

        // Trim to the allowed length
        const allowedLength = maxLength - integerPart.length;
        decimalPart = decimalPart.slice(0, allowedLength);
    }

    // Update the input value
    if (decimalPart) {
        inputElement.value = `${integerPart}.${decimalPart}`;
    } else {
        inputElement.value = integerPart;
    }

    // Final input value
    console.log("Validated Input Value:", inputElement.value);
}







