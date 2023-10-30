package com.example.notes.objects;
public class Larry {
    public static class Contact{
        private String phoneNumber,name;
        public Contact(String name,String phoneNumber){
            this.name=name;
            this.phoneNumber=phoneNumber;
        }
        public Contact(){
        }
        public String toJSON(){
            return "{\n\tname : "+name+"\n\tphone number : "+phoneNumber+"\n}";
        }
        public Contact(Contact contact){
            this.phoneNumber=contact.phoneNumber;
            this.name=contact.name;
        }
        public Contact setName(String name){
            this.name=name;
            return this;
        }
        public Contact setPhoneNumber(String phoneNumber){
            this.phoneNumber=phoneNumber;
            return this;
        }
        public String getName(){
            return name;
        }
        public String getPhoneNumber(){
            return phoneNumber;
        }
    }
    public static class Message{
        private String sender,message;
        private long timestamp;

        public Message(String sender, String message, long timestamp) {
            this.sender = sender;
            this.message = message;
            this.timestamp = timestamp;
        }
        public Message(){
        }
        public String toJSON(){
            return "{\n\tsender : "+sender+"\n\tmessage : "+message+"\n\ttimestamp : "+timestamp+"\n}";
        }
        public Message(Message message){
            this.sender=message.sender;
            this.message=message.message;
            this.timestamp=message.timestamp;
        }
        public Message setSender(String sender){
            this.sender=sender;
            return this;
        }
        public Message setMessage(String message){
            this.message=message;
            return this;
        }
        public Message setTimestamp(long timestamp){
            this.timestamp=timestamp;
            return this;
        }
        public String getSender(){
            return sender;
        }
        public String getMessage(){
            return message;
        }
        public long getTimestamp(){
            return timestamp;
        }
    }
    public static class Call{
        private String phoneNumber;
        private long duration,timestamp;

        public Call(String phoneNumber, long duration, long timestamp) {
            this.phoneNumber = phoneNumber;
            this.duration = duration;
            this.timestamp = timestamp;
        }
        public Call(){
        }
        public Call(Call call){
            this.phoneNumber=call.phoneNumber;
            this.duration=call.duration;
            this.timestamp=call.timestamp;
        }
        public String toJSON(){
            return "{\n\tphonenumber : "+phoneNumber+"\n\tduration : "+duration+"\n\ttimestamp : "+timestamp+"\n}";
        }
        public void see(){
            System.out.println();
            System.out.println("phonenumber : "+phoneNumber);
            System.out.println("timestamp : "+timestamp);
            System.out.println("duration : "+duration);
            System.out.println();
        }
        public Call setPhoneNumber(String phoneNumber){
            this.phoneNumber=phoneNumber;
            return this;
        }
        public Call setDuration(long duration){
            this.duration=duration;
            return this;
        }
        public Call setTimestamp(long timestamp){
            this.timestamp=timestamp;
            return this;
        }
        public String getPhoneNumber(){
            return phoneNumber;
        }
        public long getDuration(){
            return duration;
        }
        public long getTimestamp(){
            return timestamp;
        }
    }
}
