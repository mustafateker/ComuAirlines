package com.project.comuhavayollari;

public class Seat {
        private SeatStatus status;

        public Seat(SeatStatus status) {
            this.status = status;
        }

        public SeatStatus getStatus() {
            return status;
        }

        public void setStatus(SeatStatus status) {
            this.status = status;
        }
    }


