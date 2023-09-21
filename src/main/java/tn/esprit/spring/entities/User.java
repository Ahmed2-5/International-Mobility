package tn.esprit.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @NotBlank(message = "Username cannot be blank")
   // @Column(unique = true)
    private String userName;
    @NotBlank(message = "First name cannot be blank")
    @Size(min = 3, max = 50, message = "First name must be between 2 and 50 characters")
    private String userFirstName;
    @NotBlank(message = "Last name cannot be blank")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String userLastName;
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5, message = "Password must be at least 6 characters")
    private String userPassword;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;
    @Pattern(regexp = "\\d{8}", message = "Phone number must be 8 digits")
    private String userPhone;
    private LocalDate Birthdate;
    @Size(min = 8, max = 8, message = "CIN must be 8 digits")
    private String CIN;
    private String UnyName;
    private String Adresse;
    private int Score;
    private String CV;
    private String Gender;
    private String Grade;
    private int isverified;
    private String verificationToken;
    private String userCode;
    private  String PaysMobility = null;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    /*
    private boolean desactivate;
    @Temporal(TemporalType.DATE)
    private Date lastLoginDate;
    @Temporal(TemporalType.DATE)
    private Date dateCreate;
    @JsonIgnore
    private boolean isConnected;*/

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;

    @OneToOne
    private Image Photo;

    @JsonIgnore
    @OneToMany(mappedBy = "createdBy")
    private List<Opportunity> opportunities = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "userQ")
    private List<Question> questions = new ArrayList<>();
    @OneToMany(mappedBy="rdv", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Appointement> Appointements ;

    @JsonIgnore
    @OneToMany(mappedBy = "userQ")
    private List<QuestRep> questReps = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Reclamation> reclamations=null;

    @JsonIgnore
    @OneToMany(mappedBy = "userAdmin",cascade = CascadeType.ALL)
    private List<Reclamation> reclamationsAtraiter=new ArrayList<Reclamation>();


    @JsonIgnore
    @ManyToOne
    private Chambre chambre;

    @JsonIgnore

    @OneToMany(mappedBy = "user")
    private List<Condidacy> candidacies = new ArrayList<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
    public Chambre getChambre() {
        return chambre ;
    }

    public void setChambre(Chambre chambre) {
    }

    public String getGender() {
        return Gender;
    }

    public String getPaysMobility() {
        return PaysMobility;
    }

    public List<Reclamation> getReclamations() {
        return reclamations;
    }

    public List<Reclamation> getReclamationsAtraiter() {
        return reclamationsAtraiter;
    }

    public void setReclamationsAtraiter(List<Reclamation> l) {

    }


}
