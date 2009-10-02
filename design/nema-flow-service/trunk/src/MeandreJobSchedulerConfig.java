import java.util.List;

public interface MeandreJobSchedulerConfig {
	public MeandreServer getHeadNode();
	public List<MeandreServer> getServers();
}
