package no.evote.service;

/**
 * Used by load balancer to verify that services are up and running.
 */
public interface StatusService {
	String getStatus();
}
