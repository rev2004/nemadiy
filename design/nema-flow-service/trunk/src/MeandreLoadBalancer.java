import java.util.List;

public interface MeandreLoadBalancer {
	public MeandreServer nextAvailableServer();

	public void addServer(MeandreServer server);

	public void removeServer(MeandreServer server) throws IllegalStateException;

}