package com.touhid.project.uber.uberApp.repositories;

import com.touhid.project.uber.uberApp.entities.User;
import com.touhid.project.uber.uberApp.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Optional<Wallet> findByUser(User user);
    Optional<Wallet> findWalletByUser(User user);
}
