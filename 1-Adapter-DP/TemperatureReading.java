public interface TemperatureReading {

	public double getCelsius();
	public String getSensorInfo();
	public long getTimestamp();
	public String getStatus();

	public void setCelsius(double celsius);
	public void setSensorInfo(String sensorInfo);
	public void setTimestamp(long timestamp);
	public void setStatus(String status);
}
