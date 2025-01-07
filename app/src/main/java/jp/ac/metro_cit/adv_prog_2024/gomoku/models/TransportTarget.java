package jp.ac.metro_cit.adv_prog_2024.gomoku.models;

import java.net.InetAddress;
import javax.annotation.Nonnull;

public record TransportTarget(@Nonnull InetAddress address, int port) {}
