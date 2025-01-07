package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.net.InetAddress;
import javax.annotation.Nonnull;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
public record TransportTarget(@Nonnull InetAddress address, int port) {}
