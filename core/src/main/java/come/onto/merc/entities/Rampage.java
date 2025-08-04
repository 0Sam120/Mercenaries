package come.onto.merc.entities;

public class Rampage {
    private int maxCharges;            // Total number of segments
    private int currentFullCharges;    // Fully filled segments
    private float partialCharge;       // Progress toward the next segment (0 to 1)
    private float fillRate;            // Passive fill rate per second
    private boolean isActive;          // Whether Rampage is active
    private float durationPerCharge;   // Duration of Rampage per charge
    private float rampageTimer;        // Remaining Rampage time

    public Rampage(int maxCharges, float fillRate, float durationPerCharge) {
        this.maxCharges = maxCharges;
        this.fillRate = fillRate;
        this.durationPerCharge = durationPerCharge;
        reset();
    }

    public void reset() {
        currentFullCharges = 0;
        partialCharge = 0f;
        isActive = false;
        rampageTimer = 0f;
    }

    public void update(float deltaTime) {
        if (isActive) {
            // Countdown Rampage time
            rampageTimer -= deltaTime;
            if (rampageTimer <= 0) {
                isActive = false;
                rampageTimer = 0;
            }
        } else {
            // Passive charge filling
            partialCharge += fillRate * deltaTime;
            if (partialCharge >= 1f) {
                partialCharge -= 1f;
                currentFullCharges = Math.min(currentFullCharges + 1, maxCharges);
            }
        }
    }

    public void addCharge(float percent) {
        float totalFill = (percent / 100f) * maxCharges; // Convert to segment units
        int fullSegments = (int) totalFill;
        float partialSegment = totalFill - fullSegments;

        currentFullCharges = Math.min(currentFullCharges + fullSegments, maxCharges);

        // Handle partial charges
        if (partialSegment > 0 && currentFullCharges < maxCharges) {
            partialCharge = Math.min(partialCharge + partialSegment, 1f);

            // Overflow: convert partial to a full segment if necessary
            if (partialCharge >= 1f) {
                partialCharge -= 1f;
                currentFullCharges = Math.min(currentFullCharges + 1, maxCharges);
            }
        }
    }

    public boolean canActivate() {
        return currentFullCharges > 0 || partialCharge > 0;
    }

    public void activate() {
        if (canActivate() && !isActive) {
            isActive = true;
            rampageTimer = currentFullCharges * durationPerCharge;
            currentFullCharges = 0;
            partialCharge = 0f;
        }
    }


    public boolean isActive() {
        return isActive;
    }

    public float getProgress() {
        return currentFullCharges + partialCharge; // Total progress in segments
    }

    public int getCurrentFullCharges() {
        return currentFullCharges;
    }

    public float getPartialCharge() {
        return partialCharge;
    }

    public float getRampageTimer() {
        return rampageTimer;
    }

    public float getMaxCharges() {
        return maxCharges;
    }
}
