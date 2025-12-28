package com.vit.hostel.config;

import com.vit.hostel.entity.Hostel;
import com.vit.hostel.entity.User;
import com.vit.hostel.entity.enums.Gender;
import com.vit.hostel.entity.enums.Role;
import com.vit.hostel.repository.HostelRepository;
import com.vit.hostel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (hostelRepository.count() == 0) {
            seedHostels();
            seedAdmins();
            log.info("Data seeding completed successfully!");
        } else {
            log.info("Data already exists, skipping seeding.");
        }
    }

    private void seedHostels() {
        // Male Hostels
        createHostel("MH A", "Albert Einstein", "0416 220 2531", "mhablock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH B", "Swami Vivekananda", "0416 220 2532", "mhbblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH B ANNEX", "Swami Vivekananda – B Annex", "0416 220 2533", "mhbannex@vitadmin.ac.in",
                Gender.MALE);
        createHostel("MH C", "Rabindranath Tagore", "0416 220 2534", "mhcblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH D", "Nelson Mandela", "0416 220 2535", "mhdblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH D ANNEX", "Nelson Mandela – D Annex", "0416 220 2536", "mhdannex@vitadmin.ac.in", Gender.MALE);
        createHostel("MH E", "Sir C.V. Raman", "0416 220 2537", "mheblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH F", "Ramanujam", "0416 220 2538", "mhfblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH G", "Socrates", "0416 220 2539", "mhgblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH H", "John F Kennedy", "0416 220 2540", "mhhblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH J", "John F Kennedy", "0416 220 2541", "mhjblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH J ANNEX", "John F Kennedy – J Annex", "0416 220 2563", "mhjannexblock@vitadmin.ac.in",
                Gender.MALE);
        createHostel("MH K", "Dr. Sarvepalli Radhakrishnan", "0416 220 2542", "mhkblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH L", "Netaji Subhas Chandra Bose", "0416 220 2543", "mhlblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH M", "Quaid-E-Millat Muhammed Ismail", "0416 220 2544", "mhmblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH M ANNEX", "Quaid-E-Millat – M Annex", "0416 220 2545", "mhmannexblock@vitadmin.ac.in",
                Gender.MALE);
        createHostel("MH N", "Charles Darwin", "0416 220 2546", "mhnblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH N ANNEX", "Charles Darwin – N Annex", "0416 220 2564", "mhnannexblock@vitadmin.ac.in",
                Gender.MALE);
        createHostel("MH P", "Sardar Patel", "0416 220 2547", "mhpblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH Q", "Vajpayee Block", "0416 220 2548", "mhqblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH R", "Muthamizh Arignar Kalaignar M.Karunanidhi", "0416 220 2549", "mhrblock@vitadmin.ac.in",
                Gender.MALE);
        createHostel("MH S", "Abdul Kalam Block", "0416 220 2550", "mhsblock@vitadmin.ac.in", Gender.MALE);
        createHostel("MH T", "Jagadish Chandra Bose Block", "0416 220 2551", "mhtblock@vitadmin.ac.in", Gender.MALE);

        // Female Hostels
        createHostel("LH A", "Ladies Hostel A", "0416 220 2669", "lhablock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("LH B", "Ladies Hostel B", "0416 220 2670", "lhbblock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("LH C", "Ladies Hostel C", "0416 220 2703", "lhcblock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("LH D", "Ladies Hostel D", "0416 220 2704", "lhdblock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("LH E", "Ladies Hostel E", "0416 220 2705", "lheblock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("LH F", "Ladies Hostel F", "0416 220 2706", "lhfblock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("LH G", "Ladies Hostel G", "0416 220 2892", "lhgblock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("LH H", "Ladies Hostel H", "0416 220 2894", "lhhblock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("LH J", "Ladies Hostel J", "0416 220 2896", "lhjblock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("RGT H", "RGT Hostel H", "0416 220 5003", "rgthblock@vitadmin.ac.in", Gender.FEMALE);
        createHostel("LH GH ANNEX", "Ladies Hostel GH Annex", "0416 220 2244", "lhghannex@vitadmin.ac.in",
                Gender.FEMALE);

        log.info("Created {} hostels", hostelRepository.count());
    }

    private void createHostel(String code, String name, String phone, String adminEmail, Gender gender) {
        Hostel hostel = Hostel.builder()
                .code(code)
                .name(name)
                .phone(phone)
                .adminEmail(adminEmail)
                .gender(gender)
                .build();
        hostelRepository.save(hostel);
    }

    private void seedAdmins() {
        // Create admin accounts for each hostel
        hostelRepository.findAll().forEach(hostel -> {
            if (!userRepository.existsByEmail(hostel.getAdminEmail())) {
                User admin = User.builder()
                        .email(hostel.getAdminEmail())
                        .password(passwordEncoder.encode("admin123")) // Default password
                        .name(hostel.getCode() + " Admin")
                        .role(Role.ADMIN)
                        .hostel(hostel)
                        .roomNumber("ADMIN")
                        .build();
                userRepository.save(admin);
            }
        });

        log.info("Created {} admin accounts", userRepository.count());
    }
}
